package pedido.processador;

import java.util.List;
import java.util.Map;

import pedido.desconto.CalculoDesconto;
import pedido.frete.EstrategiaFrete;
import pedido.imposto.CalculadoraImposto;
import pedido.model.Estado;
import pedido.model.FormaPagamento;
import pedido.model.Pedido;
import pedido.model.TipoFrete;
import pedido.notificacao.CanalNotificacao;
import pedido.pagamento.MetodoPagamento;

/**
 * Template Method: define o roteiro fixo de processamento de um pedido
 * (calcular total -> processar pagamento -> notificar -> finalizar).
 *
 * As estrategias de imposto, frete e pagamento sao selecionadas a partir
 * de registros (Map) montados no Main e indexados pelos enums do Pedido,
 * o que permite adicionar novos tipos sem alterar esta classe.
 */
public class ProcessadorPedido {

    private final CalculoDesconto calculoDesconto;
    private final Map<Estado, CalculadoraImposto> calculadorasImposto;
    private final Map<TipoFrete, EstrategiaFrete> estrategiasFrete;
    private final Map<FormaPagamento, MetodoPagamento> metodosPagamento;
    private final List<CanalNotificacao> canaisNotificacao;

    public ProcessadorPedido(CalculoDesconto calculoDesconto,
                              Map<Estado, CalculadoraImposto> calculadorasImposto,
                              Map<TipoFrete, EstrategiaFrete> estrategiasFrete,
                              Map<FormaPagamento, MetodoPagamento> metodosPagamento,
                              List<CanalNotificacao> canaisNotificacao) {
        this.calculoDesconto = calculoDesconto;
        this.calculadorasImposto = calculadorasImposto;
        this.estrategiasFrete = estrategiasFrete;
        this.metodosPagamento = metodosPagamento;
        this.canaisNotificacao = canaisNotificacao;
    }

    public final void processar(Pedido pedido) {
        double total = calcularTotal(pedido);

        MetodoPagamento metodoPagamento = metodosPagamento.get(pedido.getFormaPagamento());
        if (metodoPagamento == null) {
            throw new IllegalArgumentException("Forma de pagamento nao suportada: " + pedido.getFormaPagamento());
        }
        metodoPagamento.processar(pedido, total);

        notificar(pedido);
        finalizar();
    }

    private double calcularTotal(Pedido pedido) {
        double desconto = calculoDesconto.calcular(pedido);

        CalculadoraImposto calculadoraImposto = calculadorasImposto.get(pedido.getEstado());
        if (calculadoraImposto == null) {
            throw new IllegalArgumentException("Estado sem regra de imposto cadastrada: " + pedido.getEstado());
        }
        double imposto = calculadoraImposto.calcular(pedido);

        EstrategiaFrete estrategiaFrete = estrategiasFrete.get(pedido.getTipoFrete());
        if (estrategiaFrete == null) {
            throw new IllegalArgumentException("Tipo de frete nao suportado: " + pedido.getTipoFrete());
        }
        double frete = estrategiaFrete.calcular(pedido);

        return pedido.getValorProdutos() - desconto + imposto + frete;
    }

    private void notificar(Pedido pedido) {
        for (CanalNotificacao canal : canaisNotificacao) {
            canal.notificar(pedido);
        }
    }

    private void finalizar() {
        System.out.println("Pedido processado com sucesso!");
    }
}
