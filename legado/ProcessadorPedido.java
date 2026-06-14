public class ProcessadorPedido {

    public void processar(Pedido pedido) {
        CalculadoraPedido calculadora = new CalculadoraPedido();

        double desconto = calculadora.calcularDesconto(pedido);
        double imposto = calculadora.calcularImposto(pedido);
        double frete = calculadora.calcularFrete(pedido);

        double total = pedido.getValorProdutos() - desconto + imposto + frete;

        processarPagamento(pedido, total);
        enviarNotificacoes(pedido);

        System.out.println("Pedido processado com sucesso!");
    }

    private void processarPagamento(Pedido pedido, double total) {
        if (pedido.getFormaPagamento().equals("CARTAO")) {
            System.out.println("Validando cartão...");
            System.out.println("Cobrando R$ " + total + " no cartão.");
        } else if (pedido.getFormaPagamento().equals("PIX")) {
            System.out.println("Gerando QR Code Pix...");
            System.out.println("Aguardando pagamento de R$ " + total);
        } else if (pedido.getFormaPagamento().equals("BOLETO")) {
            System.out.println("Gerando boleto...");
            System.out.println("Boleto no valor de R$ " + total);
        }
    }

    private void enviarNotificacoes(Pedido pedido) {
        System.out.println("Enviando email para " + pedido.getEmail());

        if (pedido.getTelefone() != null) {
            System.out.println("Enviando SMS para " + pedido.getTelefone());
        }
    }
}