package pedido.pagamento;

import pedido.model.Pedido;

/**
 * Template Method: define o roteiro fixo de processamento de um pagamento
 * (ajustar valor -> preparar -> cobrar -> confirmar), deixando cada meio de
 * pagamento concreto implementar apenas os passos especificos.
 */
public abstract class MetodoPagamentoTemplate implements MetodoPagamento {

    @Override
    public final void processar(Pedido pedido, double total) {
        double valorFinal = ajustarValor(total);
        preparar(pedido, valorFinal);
        cobrar(pedido, valorFinal);
        confirmar(pedido, valorFinal);
    }

    /**
     * Hook: permite que um meio de pagamento ajuste o valor final
     * (ex.: acrescimo de taxa). Por padrao, mantem o valor original.
     */
    protected double ajustarValor(double total) {
        return total;
    }

    protected abstract void preparar(Pedido pedido, double valor);

    protected abstract void cobrar(Pedido pedido, double valor);

    /**
     * Hook: passo opcional executado apos a cobranca. Por padrao, nao faz nada.
     */
    protected void confirmar(Pedido pedido, double valor) {
    }
}
