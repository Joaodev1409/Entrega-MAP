package pedido.imposto;

import pedido.model.Pedido;

public class ImpostoMG implements CalculadoraImposto {

    @Override
    public double calcular(Pedido pedido) {
        return pedido.getValorProdutos() * 0.10;
    }
}
