package pedido.desconto;

import pedido.model.Pedido;

/**
 * Componente concreto inicial da cadeia de decorators: nenhum desconto.
 */
public class DescontoBase implements CalculoDesconto {

    @Override
    public double calcular(Pedido pedido) {
        return 0;
    }
}
