package modelo;

public class Fornecedor extends Pessoa {
    private String cnpj;

    public Fornecedor(String nome, String cnpj) {
        super(nome);
        this.cnpj = cnpj;
    }

    @Override
    public String getDocumento() {
        return cnpj;
    }

    @Override
    public void setDocumento(String documento) { // <-- IMPLEMENTAÇÃO DO MÉTODO
        this.cnpj = documento;
    }
}