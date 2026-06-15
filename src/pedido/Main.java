package pedido;

import java.util.List;
import java.util.Map;

import pedido.desconto.CalculoDesconto;
import pedido.desconto.DescontoBase;
import pedido.desconto.DescontoComTeto;
import pedido.desconto.DescontoCupom;
import pedido.desconto.DescontoSazonal;
import pedido.desconto.DescontoTipoCliente;
import pedido.frete.EstrategiaFrete;
import pedido.frete.FreteExpresso;
import pedido.frete.FreteGratisDecorator;
import pedido.frete.FreteNormal;
import pedido.frete.FreteRetirada;
import pedido.imposto.CalculadoraImposto;
import pedido.imposto.ImpostoMG;
import pedido.imposto.ImpostoPadrao;
import pedido.imposto.ImpostoRJ;
import pedido.imposto.ImpostoSP;
import pedido.model.Cupom;
import pedido.model.Estado;
import pedido.model.FormaPagamento;
import pedido.model.Pedido;
import pedido.model.TipoCliente;
import pedido.model.TipoFrete;
import pedido.notificacao.CanalNotificacao;
import pedido.notificacao.NotificadorEmail;
import pedido.notificacao.NotificadorSms;
import pedido.notificacao.NotificadorWhatsapp;
import pedido.notificacao.TelefoneRequeridoProxy;
import pedido.pagamento.MetodoPagamento;
import pedido.pagamento.PagamentoBoleto;
import pedido.pagamento.PagamentoCartao;
import pedido.pagamento.PagamentoPaypal;
import pedido.pagamento.PagamentoPix;
import pedido.processador.ProcessadorPedido;

public class Main {

    public static void main(String[] args) {
        ProcessadorPedido processador = montarProcessador();

        System.out.println("=== Pedido 1: cenario original (VIP, NATAL10, PIX, NORMAL, SP) ===");
        processador.processar(new Pedido("Ana Silva", "ana@email.com", "11999998888", Estado.SP,
                TipoCliente.VIP, Cupom.NATAL10, FormaPagamento.PIX, TipoFrete.NORMAL, 500.0, true, false));

        System.out.println("\n=== Pedido 2: cliente PREMIUM, cupom FRETEGRATIS, frete NORMAL (frete zerado) ===");
        processador.processar(new Pedido("Bruno Costa", "bruno@email.com", "21988887777", Estado.RJ,
                TipoCliente.PREMIUM, Cupom.FRETEGRATIS, FormaPagamento.CARTAO, TipoFrete.NORMAL, 300.0, false, false));

        System.out.println("\n=== Pedido 3: cupom FRETEGRATIS + frete EXPRESSO (frete mantido) ===");
        processador.processar(new Pedido("Carla Dias", "carla@email.com", null, Estado.MG,
                TipoCliente.COMUM, Cupom.FRETEGRATIS, FormaPagamento.BOLETO, TipoFrete.EXPRESSO, 200.0, false, false));

        System.out.println("\n=== Pedido 4: pagamento via PAYPAL (taxa de 3%), sem telefone (SMS/WhatsApp nao enviados) ===");
        processador.processar(new Pedido("Daniela Souza", "daniela@email.com", null, Estado.OUTRO,
                TipoCliente.FUNCIONARIO, Cupom.NENHUM, FormaPagamento.PAYPAL, TipoFrete.RETIRADA, 400.0, false, false));

        System.out.println("\n=== Pedido 5: funcionario + cupom BLACKFRIDAY + campanha sazonal (teto de 40%) ===");
        processador.processar(new Pedido("Eduardo Lima", "eduardo@email.com", "31977776666", Estado.SP,
                TipoCliente.FUNCIONARIO, Cupom.BLACKFRIDAY, FormaPagamento.PIX, TipoFrete.NORMAL, 1000.0, false, true));
    }

    /**
     * Monta o ProcessadorPedido com todas as estrategias, decorators e
     * observadores disponiveis. Adicionar um novo tipo (pagamento, frete,
     * imposto, cupom ou canal de notificacao) significa apenas criar a nova
     * classe e registra-la aqui, sem alterar o ProcessadorPedido.
     */
    private static ProcessadorPedido montarProcessador() {
        CalculoDesconto calculoDesconto = new DescontoComTeto(
                new DescontoSazonal(
                        new DescontoCupom(
                                new DescontoTipoCliente(
                                        new DescontoBase()))));

        Map<Estado, CalculadoraImposto> calculadorasImposto = Map.of(
                Estado.SP, new ImpostoSP(),
                Estado.RJ, new ImpostoRJ(),
                Estado.MG, new ImpostoMG(),
                Estado.OUTRO, new ImpostoPadrao());

        Map<TipoFrete, EstrategiaFrete> estrategiasFrete = Map.of(
                TipoFrete.NORMAL, new FreteGratisDecorator(new FreteNormal()),
                TipoFrete.EXPRESSO, new FreteGratisDecorator(new FreteExpresso()),
                TipoFrete.RETIRADA, new FreteGratisDecorator(new FreteRetirada()));

        Map<FormaPagamento, MetodoPagamento> metodosPagamento = Map.of(
                FormaPagamento.CARTAO, new PagamentoCartao(),
                FormaPagamento.PIX, new PagamentoPix(),
                FormaPagamento.BOLETO, new PagamentoBoleto(),
                FormaPagamento.PAYPAL, new PagamentoPaypal());

        List<CanalNotificacao> canaisNotificacao = List.of(
                new NotificadorEmail(),
                new TelefoneRequeridoProxy(new NotificadorSms()),
                new TelefoneRequeridoProxy(new NotificadorWhatsapp()));

        return new ProcessadorPedido(calculoDesconto, calculadorasImposto, estrategiasFrete,
                metodosPagamento, canaisNotificacao);
    }
}
