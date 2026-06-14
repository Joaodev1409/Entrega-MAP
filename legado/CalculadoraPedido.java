public class CalculadoraPedido {

    public double calcularDesconto(Pedido pedido) {
        double desconto = 0;

        if (pedido.getTipoCliente().equals("COMUM")) {
            desconto = 0;
        } else if (pedido.getTipoCliente().equals("VIP")) {
            desconto = pedido.getValorProdutos() * 0.10;
        } else if (pedido.getTipoCliente().equals("FUNCIONARIO")) {
            desconto = pedido.getValorProdutos() * 0.30;
        }

        if (pedido.getCupom() != null && pedido.getCupom().equals("NATAL10")) {
            desconto += pedido.getValorProdutos() * 0.10;
        } else if (pedido.getCupom() != null && pedido.getCupom().equals("BLACKFRIDAY")) {
            desconto += pedido.getValorProdutos() * 0.25;
        }

        return desconto;
    }

    public double calcularImposto(Pedido pedido) {
        if (pedido.getEstado().equals("SP")) {
            return pedido.getValorProdutos() * 0.12;
        } else if (pedido.getEstado().equals("RJ")) {
            return pedido.getValorProdutos() * 0.15;
        } else if (pedido.getEstado().equals("MG")) {
            return pedido.getValorProdutos() * 0.10;
        }

        return pedido.getValorProdutos() * 0.08;
    }

    public double calcularFrete(Pedido pedido) {
        double frete = 0;

        if (pedido.getTipoFrete().equals("NORMAL")) {
            frete = 20;
        } else if (pedido.getTipoFrete().equals("EXPRESSO")) {
            frete = 50;
        } else if (pedido.getTipoFrete().equals("RETIRADA")) {
            frete = 0;
        }

        if (pedido.isEmbrulhoPresente()) {
            frete += 5;
        }

        return frete;
    }
}