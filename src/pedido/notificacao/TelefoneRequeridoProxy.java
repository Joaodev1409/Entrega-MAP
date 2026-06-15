package pedido.notificacao;

import pedido.model.Pedido;

/**
 * Proxy de protecao: so delega a notificacao ao canal real se o pedido
 * possuir telefone cadastrado. Usado para envolver canais como SMS e WhatsApp.
 */
public class TelefoneRequeridoProxy implements CanalNotificacao {

    private final CanalNotificacao canalReal;

    public TelefoneRequeridoProxy(CanalNotificacao canalReal) {
        this.canalReal = canalReal;
    }

    @Override
    public void notificar(Pedido pedido) {
        if (pedido.getTelefone() != null) {
            canalReal.notificar(pedido);
        }
    }
}
