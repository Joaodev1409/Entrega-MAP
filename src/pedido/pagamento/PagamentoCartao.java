package pedido.pagamento;

import pedido.model.Pedido;

public class PagamentoCartao extends MetodoPagamentoTemplate {

    @Override
    protected void preparar(Pedido pedido, double valor) {
        System.out.println("Validando cartão...");
    }

    @Override
    protected void cobrar(Pedido pedido, double valor) {
        System.out.println("Cobrando R$ " + valor + " no cartão.");
    }
}
