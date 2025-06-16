package modelo;

public class Produto {
    private static int contador = 1;
    private int codigo;
    private String descricao;
    private double custo;
    private double precoVenda;
    private int idFornecedor; 
    private boolean ativo;

    public Produto(String descricao, double custo, double precoVenda, int idFornecedor) {
        this.codigo = contador++;
        this.descricao = descricao;
        this.custo = custo;
        this.precoVenda = precoVenda;
        this.idFornecedor = idFornecedor;
        this.ativo = true;
    }


    public int getCodigo() { return codigo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getCusto() { return custo; }
    public void setCusto(double custo) { this.custo = custo; }
    public double getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(double precoVenda) { this.precoVenda = precoVenda; }
    public int getIdFornecedor() { return idFornecedor; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}