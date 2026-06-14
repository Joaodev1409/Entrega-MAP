public class Main {
    public static void main(String[] args) {
        Pedido pedido = new Pedido("Ana Silva", "ana@email.com", "11999998888", "SP", "VIP", "NATAL10", "PIX", "NORMAL", 500.0, true);

        ProcessadorPedido processador = new ProcessadorPedido();
        processador.processar(pedido);
    }
}