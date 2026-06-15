package pedido.frete;

import pedido.model.Pedido;

public class FreteExpresso extends FreteBase {

    @Override
    protected double valorBase(Pedido pedido) {
        return 50;
    }
}
