package pedido.frete;

import pedido.model.Pedido;

public class FreteRetirada extends FreteBase {

    @Override
    protected double valorBase(Pedido pedido) {
        return 0;
    }
}
