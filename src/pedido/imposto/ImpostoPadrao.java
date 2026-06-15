package pedido.imposto;

import pedido.model.Pedido;

/**
 * Regra fiscal aplicada aos estados que nao possuem uma aliquota especifica.
 */
public class ImpostoPadrao implements CalculadoraImposto {

    @Override
    public double calcular(Pedido pedido) {
        return pedido.getValorProdutos() * 0.08;
    }
}
