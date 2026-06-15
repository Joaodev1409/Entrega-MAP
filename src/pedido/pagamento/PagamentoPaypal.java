package pedido.pagamento;

import pedido.model.Pedido;

/**
 * Pagamento via PayPal: cobra uma taxa adicional de 3% sobre o total final,
 * aplicada atraves do hook ajustarValor() do Template Method.
 */
public class PagamentoPaypal extends MetodoPagamentoTemplate {

    private static final double TAXA_PAYPAL = 0.03;

    @Override
    protected double ajustarValor(double total) {
        return total * (1 + TAXA_PAYPAL);
    }

    @Override
    protected void preparar(Pedido pedido, double valor) {
        System.out.println("Redirecionando para o PayPal...");
    }

    @Override
    protected void cobrar(Pedido pedido, double valor) {
        System.out.println("Cobrando R$ " + valor + " via PayPal (taxa de 3% incluida).");
    }
}
