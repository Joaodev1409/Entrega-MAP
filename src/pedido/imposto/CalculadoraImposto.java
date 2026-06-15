package pedido.imposto;

import pedido.model.Pedido;

/**
 * Strategy: calcula o imposto aplicavel ao pedido de acordo com a regra
 * fiscal de um estado especifico.
 */
public interface CalculadoraImposto {
    double calcular(Pedido pedido);
}
