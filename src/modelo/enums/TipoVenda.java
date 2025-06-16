package modelo.enums;

public enum TipoVenda {
    PRESENCIAL("Venda Presencial"),
    ENTREGA("Venda com Entrega");

    private final String descricao;

    TipoVenda(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}