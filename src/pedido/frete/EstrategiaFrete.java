package pedido.frete;

import pedido.model.Pedido;

/**
 * Strategy: calcula o valor do frete de acordo com o tipo escolhido no pedido.
 */
public interface EstrategiaFrete {
    double calcular(Pedido pedido);
}
