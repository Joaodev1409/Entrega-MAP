package pedido.desconto;

import pedido.model.Pedido;

/**
 * Componente do Decorator: calcula o valor de desconto aplicavel a um pedido.
 */
public interface CalculoDesconto {
    double calcular(Pedido pedido);
}
