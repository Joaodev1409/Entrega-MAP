package pedido.notificacao;

import pedido.model.Pedido;

public class NotificadorSms implements CanalNotificacao {

    @Override
    public void notificar(Pedido pedido) {
        System.out.println("Enviando SMS para " + pedido.getTelefone());
    }
}
