# Relatório de Diagnóstico — Sistema Legado de Pedidos

Análise das classes `Pedido`, `CalculadoraPedido` e `ProcessadorPedido`, identificando os
principais problemas estruturais que dificultam manutenção, testes e evolução do sistema.


## 1. Forma de pagamento via cadeia de if-else (`ProcessadorPedido.processarPagamento`)
Cada novo meio de pagamento (ex.: PAYPAL) exigiria editar o processador principal,
violando o Princípio Aberto/Fechado (OCP) e o requisito (a).
**Padrão sugerido:** **Strategy** — cada forma de pagamento (Cartão, Pix, Boleto, Paypal)
implementa uma interface comum `MetodoPagamento`. Opcionalmente, **Template Method**
define o roteiro comum (validar → cobrar/gerar → confirmar), e cada estratégia
implementa apenas os passos específicos.

## 2. Cálculo de frete via cadeia de if-else (`CalculadoraPedido.calcularFrete`)
Tipos de frete ("NORMAL", "EXPRESSO", "RETIRADA") estão hardcoded; adicionar um novo
tipo exige alterar a classe central de cálculo, violando OCP e o requisito (b).
**Padrão sugerido:** **Strategy** (`EstrategiaFrete` por tipo, incluindo o futuro
FRETEGRATIS).

## 3. Cálculo de imposto acoplado a regras fixas por estado (`CalculadoraPedido.calcularImposto`)
Alíquotas de SP/RJ/MG estão embutidas em if-else com fallback genérico; uma nova regra
fiscal por estado não pode ser isolada/testada separadamente (requisito d).
**Padrão sugerido:** **Strategy** (`CalculadoraImposto` por estado, selecionada via
registro/mapa).

## 4. Descontos não combináveis e sem limite de negócio (`CalculadoraPedido.calcularDesconto`)
Desconto por cliente e por cupom são somados de forma fixa dentro do mesmo método;
não há mecanismo para compor novas regras (ex.: campanha sazonal) nem para aplicar o
teto de 40% exigido. Viola OCP e o requisito (f).
**Padrão sugerido:** **Decorator** — cada regra de desconto "decora" o cálculo
anterior, permitindo composição dinâmica; o decorator mais externo aplica o teto
de 40% sobre o valor dos produtos.

## 5. Notificações fixas e não extensíveis (`ProcessadorPedido.enviarNotificacoes`)
Email é sempre enviado e SMS é condicional, ambos hardcoded no método. Adicionar
WhatsApp (com sua própria regra de envio) exigiria alterar essa classe, violando o
requisito (e).
**Padrão sugerido:** **Observer** — o pedido notifica uma lista de observadores
(Email, SMS, WhatsApp) registrados dinamicamente. Para a regra "WhatsApp só envia se
houver telefone cadastrado", um **Proxy** pode envolver o observador de WhatsApp,
verificando a pré-condição antes de delegar ao envio real (protection proxy).

## 6. Strings mágicas espalhadas pelo sistema
Valores como "CARTAO", "PIX", "VIP", "COMUM", "FUNCIONARIO", "SP", "NATAL10",
"BLACKFRIDAY", "NORMAL"/"EXPRESSO" aparecem como literais repetidos em várias classes,
sem checagem em tempo de compilação — atende ao requisito (h) de eliminação.
**Abordagem:** não é um padrão GoF, mas é pré-requisito para os Strategies acima —
usar **Enums/Value Objects** (`TipoCliente`, `FormaPagamento`, `TipoFrete`, `Estado`,
`Cupom`) elimina comparações por string e viabiliza os registros de estratégia.

## 7. `ProcessadorPedido` acumula múltiplas responsabilidades (violação SRP)
A classe orquestra cálculo, pagamento e notificação no mesmo lugar — qualquer mudança em
uma dessas áreas tem potencial de impactar as outras (requisito g).
**Padrão sugerido:** **Template Method** — o fluxo fixo "calcular total → processar
pagamento → enviar notificações" é modelado como o esqueleto do algoritmo, delegando
cada etapa às abstrações de Strategy/Observer/Decorator dos itens anteriores.
`ProcessadorPedido` passa a ser apenas o orquestrador do template.

## 8. Dependência direta de classes concretas (`new CalculadoraPedido()`)
`processar()` instancia `CalculadoraPedido` diretamente, impedindo substituição/mocagem
em testes e acoplando o processador a uma implementação específica.
**Abordagem:** consequência natural da extração de interfaces nos itens 1-5 —
`ProcessadorPedido` passa a depender de abstrações (`MetodoPagamento`,
`EstrategiaFrete`, `CalculadoraImposto`, lista de observadores), recebidas via
construtor, em vez de instanciar implementações concretas.

## 9. Saída via `System.out.println` em vez de resultado verificável
Pagamento e notificações são "simulados" apenas com prints; não há objeto de retorno com
status, o que torna o comportamento impossível de testar automaticamente e mistura
lógica de negócio com I/O.
**Padrão sugerido:** **Proxy** — um proxy de pagamento/notificação pode interceptar a
chamada real para registrar logs/validações, ou ser substituído por uma versão de
teste, sem alterar a lógica de negócio das Strategies/Observers.

## 10. Ausência de tratamento para valores desconhecidos (falha silenciosa)
Se `formaPagamento` ou `tipoFrete` não corresponderem a nenhum valor esperado
(ex.: erro de digitação), nada acontece — nenhuma exceção, nenhum log de erro,
nenhum valor default explícito.
**Padrão sugerido:** tratado dentro do mecanismo de seleção de **Strategy** — se não
houver estratégia registrada para o valor informado, lança-se uma exceção explícita
em vez de simplesmente não fazer nada.

---

### Resumo — padrões aplicáveis por problema

| # | Problema | Padrão sugerido |
|---|----------|------------------|
| 1 | Pagamento em if-else | Strategy (+ Template Method) |
| 2 | Frete em if-else | Strategy |
| 3 | Imposto por estado | Strategy |
| 4 | Descontos não combináveis | Decorator |
| 5 | Notificações fixas | Observer (+ Proxy p/ guarda do WhatsApp) |
| 6 | Strings mágicas | Enum / Value Object (apoio) |
| 7 | God class (SRP) | Template Method |
| 8 | Acoplamento concreto (DIP) | Consequência das interfaces dos itens 1-5 |
| 9 | Saída via println | Proxy |
| 10 | Falha silenciosa | Strategy (registro com exceção) |

---

## Parte 2 — Justificativa da Refatoração

O código refatorado está em `src/pedido/`, organizado em pacotes por responsabilidade/padrão. O
código legado original foi preservado em `legado/` como referência do diagnóstico.

### `model/` — Enums e Value Objects (resolve o problema 6)
`TipoCliente`, `FormaPagamento`, `TipoFrete`, `Estado` e `Cupom` substituem todas as strings
mágicas do código legado ("VIP", "PIX", "SP", "NATAL10" etc.). `Pedido` passa a ser construído
com esses tipos, eliminando comparações por `.equals()` em strings e erros de digitação.

### `desconto/` — Decorator (resolve o problema 4)
- `CalculoDesconto` é o componente; `DescontoBase` retorna 0.
- `DescontoTipoCliente`, `DescontoCupom` e `DescontoSazonal` são decorators que somam sua
  regra ao valor já calculado pelo decorator anterior, permitindo **combinar múltiplos
  descontos dinamicamente** (requisito f e funcionalidade extra 2) sem alterar classes
  existentes.
- `DescontoComTeto` é o decorator mais externo e garante que a soma nunca passe de **40%** do
  valor dos produtos (funcionalidade extra 2).
- O novo tipo **PREMIUM** (15%, funcionalidade extra a) entrou apenas como um `case` em
  `DescontoTipoCliente`, sem tocar nas demais classes.

### `frete/` — Strategy + Decorator (resolve o problema 2)
- `EstrategiaFrete` é a interface Strategy; `FreteNormal`, `FreteExpresso` e `FreteRetirada`
  implementam cada tipo (via `FreteBase`, que centraliza a taxa de embrulho).
- Um novo tipo de frete (requisito b) é apenas uma nova classe `EstrategiaFrete` registrada no
  `Map` do `Main`, sem alterar o `ProcessadorPedido`.
- `FreteGratisDecorator` implementa o cupom **FRETEGRATIS** (zera o frete, exceto se
  EXPRESSO — funcionalidade extra b) sem alterar as estratégias existentes: o mesmo padrão
  Decorator usado nos descontos é reaproveitado para um problema diferente.

### `imposto/` — Strategy (resolve o problema 3)
`CalculadoraImposto` define o contrato; `ImpostoSP`, `ImpostoRJ`, `ImpostoMG` e `ImpostoPadrao`
isolam cada regra fiscal (requisito d). Uma nova regra por estado é uma nova classe + uma
entrada no `Map<Estado, CalculadoraImposto>`.

### `pagamento/` — Strategy + Template Method (resolve o problema 1)
- `MetodoPagamento` é o contrato Strategy.
- `MetodoPagamentoTemplate` define o Template Method `processar()`, fixando o roteiro
  `ajustarValor → preparar → cobrar → confirmar`.
- `PagamentoCartao`, `PagamentoPix` e `PagamentoBoleto` implementam apenas `preparar`/`cobrar`,
  preservando exatamente as mensagens do sistema original (requisito i).
- `PagamentoPaypal` (funcionalidade extra c) reaproveita o mesmo template e sobrescreve apenas
  o hook `ajustarValor` para aplicar a taxa de 3%.
- Adicionar uma nova forma de pagamento (requisito a) não exige alterar o `ProcessadorPedido`:
  basta criar a classe e registrá-la no `Map<FormaPagamento, MetodoPagamento>`.

### `notificacao/` — Observer + Proxy (resolve o problema 5)
- `CanalNotificacao` é o observador; `NotificadorEmail`, `NotificadorSms` e
  `NotificadorWhatsapp` (requisito e e funcionalidade extra d) implementam cada canal.
- `TelefoneRequeridoProxy` é um proxy de proteção genérico, reaproveitado tanto para SMS quanto
  para WhatsApp, garantindo que só enviem se o pedido tiver telefone cadastrado — sem duplicar
  a verificação em cada notificador.
- Novos canais são adicionados apenas registrando-os na lista de observadores no `Main`.

### `processador/ProcessadorPedido` — Template Method (resolve os problemas 7 e 8)
`processar()` é `final` e fixa o algoritmo: calcular total → processar pagamento → notificar →
finalizar (requisito g). As variações (qual imposto, frete, pagamento, desconto e canais usar)
vêm de abstrações injetadas via construtor — a classe nunca instancia implementações
concretas, resolvendo o acoplamento direto (`new CalculadoraPedido()`) do código legado.

### Seleção de estratégias via Map (resolve o problema 10)
Cada `Map` (`calculadorasImposto`, `estrategiasFrete`, `metodosPagamento`) é montado uma única
vez no `Main` e consultado pelo enum do `Pedido`. Se não houver entrada para o valor informado,
`ProcessadorPedido` lança `IllegalArgumentException` explícita, eliminando a falha silenciosa
do código legado.

### Saída via `println` (problema 9)
A saída via `System.out.println` foi mantida para preservar o comportamento observável original
(requisito i), mas agora está isolada dentro de cada Strategy/Observer — pode ser substituída
por um Proxy de log/teste ou por um objeto de resultado sem alterar a lógica de negócio.

### Resumo — onde cada padrão foi aplicado

| Padrão | Onde | Problema(s) do diagnóstico resolvido(s) |
|---|---|---|
| Enum / Value Object | `model/` | 6 |
| Decorator | `desconto/` (descontos combináveis + teto de 40%), `frete/FreteGratisDecorator` | 2, 4 |
| Strategy | `frete/`, `imposto/`, `pagamento/` | 1, 2, 3 |
| Template Method | `pagamento/MetodoPagamentoTemplate`, `processador/ProcessadorPedido` | 1, 7, 8 |
| Observer | `notificacao/` | 5 |
| Proxy | `notificacao/TelefoneRequeridoProxy` | 5, 9 |

### Validação
O `Main.java` executa 5 cenários cobrindo o comportamento original (Pedido 1, idêntico ao
legado) e todas as funcionalidades extras: cliente PREMIUM, cupom FRETEGRATIS (com e sem frete
EXPRESSO), pagamento PAYPAL com taxa, canais sem telefone (SMS/WhatsApp não enviados) e o teto
de 40% em descontos combinados.
