package modelo;

// No seu PDF, Endereço tem mais campos. Vamos atualizá-lo.
public class Endereco {
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String tipo; // Ex: comercial, residencial [cite: 2]

    public Endereco(String cep, String logradouro, String numero, String complemento, String tipo) {
        this.cep = cep;
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Tipo: " + tipo + " | " + logradouro + ", " + numero + " (CEP: " + cep + ")";
    }
}