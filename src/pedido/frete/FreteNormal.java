package pedido.frete;

import pedido.model.Pedido;

public class FreteNormal extends FreteBase {

    @Override
    protected double valorBase(Pedido pedido) {
        return 20;
    }
}
