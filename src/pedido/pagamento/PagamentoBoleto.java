package pedido.pagamento;

import pedido.model.Pedido;

public class PagamentoBoleto extends MetodoPagamentoTemplate {

    @Override
    protected void preparar(Pedido pedido, double valor) {
        System.out.println("Gerando boleto...");
    }

    @Override
    protected void cobrar(Pedido pedido, double valor) {
        System.out.println("Boleto no valor de R$ " + valor);
    }
}
