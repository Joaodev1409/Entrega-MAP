package pedido.desconto;

import pedido.model.Pedido;

/**
 * Decorator final da cadeia: garante que a soma de todos os descontos
 * combinados nunca ultrapasse o teto definido (40% do valor dos produtos).
 */
public class DescontoComTeto extends DescontoDecorator {

    private static final double TETO_PERCENTUAL = 0.40;

    public DescontoComTeto(CalculoDesconto descontoAnterior) {
        super(descontoAnterior);
    }

    @Override
    public double calcular(Pedido pedido) {
        double total = super.calcular(pedido);
        double teto = pedido.getValorProdutos() * TETO_PERCENTUAL;
        return Math.min(total, teto);
    }

    @Override
    protected double calcularAdicional(Pedido pedido) {
        return 0;
    }
}
