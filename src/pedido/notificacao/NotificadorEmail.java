package pedido.notificacao;

import pedido.model.Pedido;

public class NotificadorEmail implements CanalNotificacao {

    @Override
    public void notificar(Pedido pedido) {
        System.out.println("Enviando email para " + pedido.getEmail());
    }
}
