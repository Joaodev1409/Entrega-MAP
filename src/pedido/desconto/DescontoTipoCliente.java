package pedido.desconto;

import pedido.model.Pedido;

/**
 * Adiciona o desconto percentual de acordo com o tipo de cliente do pedido.
 */
public class DescontoTipoCliente extends DescontoDecorator {

    public DescontoTipoCliente(CalculoDesconto descontoAnterior) {
        super(descontoAnterior);
    }

    @Override
    protected double calcularAdicional(Pedido pedido) {
        switch (pedido.getTipoCliente()) {
            case VIP:
                return pedido.getValorProdutos() * 0.10;
            case FUNCIONARIO:
                return pedido.getValorProdutos() * 0.30;
            case PREMIUM:
                return pedido.getValorProdutos() * 0.15;
            case COMUM:
            default:
                return 0;
        }
    }
}
