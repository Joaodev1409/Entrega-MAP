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
