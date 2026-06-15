package pedido.desconto;

import pedido.model.Pedido;

/**
 * Adiciona o desconto percentual referente ao cupom informado no pedido.
 * Cupons que nao concedem desconto direto (ex: FRETEGRATIS) nao somam nada aqui.
 */
public class DescontoCupom extends DescontoDecorator {

    public DescontoCupom(CalculoDesconto descontoAnterior) {
        super(descontoAnterior);
    }

    @Override
    protected double calcularAdicional(Pedido pedido) {
        switch (pedido.getCupom()) {
            case NATAL10:
                return pedido.getValorProdutos() * 0.10;
            case BLACKFRIDAY:
                return pedido.getValorProdutos() * 0.25;
            default:
                return 0;
        }
    }
}
