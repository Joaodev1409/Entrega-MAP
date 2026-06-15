package pedido.frete;

import pedido.model.Cupom;
import pedido.model.Pedido;
import pedido.model.TipoFrete;

/**
 * Decorator: zera o valor do frete calculado quando o cupom FRETEGRATIS
 * esta presente, exceto se o tipo de frete for EXPRESSO.
 */
public class FreteGratisDecorator implements EstrategiaFrete {

    private final EstrategiaFrete freteAnterior;

    public FreteGratisDecorator(EstrategiaFrete freteAnterior) {
        this.freteAnterior = freteAnterior;
    }

    @Override
    public double calcular(Pedido pedido) {
        double valor = freteAnterior.calcular(pedido);

        if (pedido.getCupom() == Cupom.FRETEGRATIS && pedido.getTipoFrete() != TipoFrete.EXPRESSO) {
            return 0;
        }

        return valor;
    }
}
