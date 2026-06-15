package pedido.notificacao;

import pedido.model.Pedido;

/**
 * Observer: representa um canal que e notificado quando um pedido e processado.
 */
public interface CanalNotificacao {
    void notificar(Pedido pedido);
}
