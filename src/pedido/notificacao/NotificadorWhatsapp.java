package pedido.notificacao;

import pedido.model.Pedido;

public class NotificadorWhatsapp implements CanalNotificacao {

    @Override
    public void notificar(Pedido pedido) {
        System.out.println("Enviando WhatsApp para " + pedido.getTelefone());
    }
}
