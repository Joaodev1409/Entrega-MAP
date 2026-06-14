public class Pedido {
    private String cliente;
    private String email;
    private String telefone;
    private String estado;
    private String tipoCliente;
    private String cupom;
    private String formaPagamento;
    private String tipoFrete;
    private double valorProdutos;
    private boolean embrulhoPresente;

    public Pedido(String cliente, String email, String telefone, String estado, String tipoCliente, String cupom, String formaPagamento, String tipoFrete, double valorProdutos,           
                  boolean embrulhoPresente) {
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

    public String getEstado() {
        return estado;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public String getCupom() {
        return cupom;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public String getTipoFrete() {
        return tipoFrete;
    }

    public double getValorProdutos() {
        return valorProdutos;
    }

    public boolean isEmbrulhoPresente() {
        return embrulhoPresente;
    }
}