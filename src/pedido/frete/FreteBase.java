package pedido.frete;

import pedido.model.Pedido;

/**
 * Base comum das estrategias de frete: soma a taxa de embrulho ao valor
 * base definido por cada tipo de frete concreto.
 */
public abstract class FreteBase implements EstrategiaFrete {

    private static final double TAXA_EMBRULHO = 5;

    @Override
    public final double calcular(Pedido pedido) {
        double valor = valorBase(pedido);
        if (pedido.isEmbrulhoPresente()) {
            valor += TAXA_EMBRULHO;
        }
        return valor;
    }

    protected abstract double valorBase(Pedido pedido);
}
