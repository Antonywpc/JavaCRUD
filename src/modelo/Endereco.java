package modelo;
import modelo.enums.TipoEndereco;

public class Endereco {
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private TipoEndereco tipo;

    public Endereco(String cep, String logradouro, String numero, String complemento, TipoEndereco tipo) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Tipo: " + tipo.getDescricao() + " | " + logradouro + ", " + numero + " (CEP: " + cep + ")";
    }
}