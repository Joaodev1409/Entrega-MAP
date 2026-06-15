package pedido.model;

public class Pedido {
    private final String cliente;
    private final String email;
    private final String telefone;
    private final Estado estado;
    private final TipoCliente tipoCliente;
    private final Cupom cupom;
    private final FormaPagamento formaPagamento;
    private final TipoFrete tipoFrete;
    private final double valorProdutos;
    private final boolean embrulhoPresente;
    private final boolean campanhaSazonalAtiva;

    public Pedido(String cliente, String email, String telefone, Estado estado, TipoCliente tipoCliente,
                   Cupom cupom, FormaPagamento formaPagamento, TipoFrete tipoFrete, double valorProdutos,
                   boolean embrulhoPresente, boolean campanhaSazonalAtiva) {
        this.cliente = cliente;
        this.email = email;
        this.telefone = telefone;
        this.estado = estado;
        this.tipoCliente = tipoCliente;
        this.cupom = cupom;
        this.formaPagamento = formaPagamento;
        this.tipoFrete = tipoFrete;
        this.valorProdutos = valorProdutos;
        this.embrulhoPresente = embrulhoPresente;
        this.campanhaSazonalAtiva = campanhaSazonalAtiva;
    }

    public String getCliente() {
        return cliente;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public Estado getEstado() {
        return estado;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public Cupom getCupom() {
        return cupom;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public TipoFrete getTipoFrete() {
        return tipoFrete;
    }

    public double getValorProdutos() {
        return valorProdutos;
    }

    public boolean isEmbrulhoPresente() {
        return embrulhoPresente;
    }

    public boolean isCampanhaSazonalAtiva() {
        return campanhaSazonalAtiva;
    }
}
