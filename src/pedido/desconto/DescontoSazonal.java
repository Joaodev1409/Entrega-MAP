package pedido.desconto;

import pedido.model.Pedido;

/**
 * Exemplo de regra de desconto adicional e independente: campanha sazonal.
 * Demonstra como uma nova promocao pode ser somada as demais sem alterar
 * nenhuma das classes de desconto existentes.
 */
public class DescontoSazonal extends DescontoDecorator {

    private static final double PERCENTUAL_CAMPANHA = 0.05;

    public DescontoSazonal(CalculoDesconto descontoAnterior) {
        super(descontoAnterior);
    }

    @Override
    protected double calcularAdicional(Pedido pedido) {
        if (pedido.isCampanhaSazonalAtiva()) {
            return pedido.getValorProdutos() * PERCENTUAL_CAMPANHA;
        }
        return 0;
    }
}
