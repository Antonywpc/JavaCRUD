package modelo;

public class ItemVenda {
    private int idProduto;
    private int quantidade;
    private double precoUnitario;

    public ItemVenda(int idProduto, int quantidade, double precoUnitario) {
        this.idProduto = idProduto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }
    
    public double getValorTotalItem() {
        return this.quantidade * this.precoUnitario;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;   
    }
}

