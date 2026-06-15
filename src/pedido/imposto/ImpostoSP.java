package pedido.imposto;

import pedido.model.Pedido;

public class ImpostoSP implements CalculadoraImposto {

    @Override
    public double calcular(Pedido pedido) {
        return pedido.getValorProdutos() * 0.12;
    }
}
