package pedido.pagamento;

import pedido.model.Pedido;

public class PagamentoPix extends MetodoPagamentoTemplate {

    @Override
    protected void preparar(Pedido pedido, double valor) {
        System.out.println("Gerando QR Code Pix...");
    }

    @Override
    protected void cobrar(Pedido pedido, double valor) {
        System.out.println("Aguardando pagamento de R$ " + valor);
    }
}
