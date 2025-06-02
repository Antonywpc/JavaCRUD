package modelo;

public class Cliente extends Pessoa {
    private String cpf;

    public Cliente(String nome, String cpf) {
        super(nome);
        this.cpf = cpf;
    }

    @Override
    public String getDocumento() {
        return cpf;
    }

    @Override
    public void setDocumento(String documento) { // <-- IMPLEMENTAÇÃO DO MÉTODO
        this.cpf = documento;
    }
}