package modelo.enums;

public enum TipoEndereco {
    RESIDENCIAL("Residencial"),
    COMERCIAL("Comercial"),
    FISCAL("Fiscal"),
    ENTREGA("Entrega"),
    CORRESPONDENCIA("Correspondência");

    private final String descricao;

    TipoEndereco(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoEndereco fromDescricao(String input) {
        for (TipoEndereco tipo : TipoEndereco.values()) {
            if (tipo.descricao.equalsIgnoreCase(input.trim())) {
                return tipo;
            }
        }
        return null; 
    }

    public static void listarTipos() {
        for (TipoEndereco tipo : TipoEndereco.values()) {
            System.out.println("- " + tipo.getDescricao());
        }
    }
}
