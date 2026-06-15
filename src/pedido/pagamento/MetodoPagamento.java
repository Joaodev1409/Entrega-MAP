package pedido.pagamento;

import pedido.model.Pedido;

/**
 * Strategy: processa o pagamento do pedido por um meio especifico.
 */
public interface MetodoPagamento {
    void processar(Pedido pedido, double total);
}
