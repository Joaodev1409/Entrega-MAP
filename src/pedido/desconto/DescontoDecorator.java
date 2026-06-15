package pedido.desconto;

import pedido.model.Pedido;

/**
 * Decorator abstrato: encapsula um CalculoDesconto e soma sua propria regra
 * ao valor calculado pelo componente decorado.
 */
public abstract class DescontoDecorator implements CalculoDesconto {

    private final CalculoDesconto descontoAnterior;

    protected DescontoDecorator(CalculoDesconto descontoAnterior) {
        this.descontoAnterior = descontoAnterior;
    }

    @Override
    public double calcular(Pedido pedido) {
        return descontoAnterior.calcular(pedido) + calcularAdicional(pedido);
    }

    protected abstract double calcularAdicional(Pedido pedido);
}
