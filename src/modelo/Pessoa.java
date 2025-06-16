package modelo;

import java.util.ArrayList;
import java.util.List;


public abstract class Pessoa {
    private static int contador = 1;
    private int id;
    private String nome;
    private boolean ativo;
    private List<Endereco> enderecos; 

    public Pessoa(String nome) {
        this.id = contador++;
        this.nome = nome;
        this.ativo = true;
        this.enderecos = new ArrayList<>(); 
    }


    public void adicionarEndereco(Endereco endereco) {
        this.enderecos.add(endereco);
    }

    public List<Endereco> getEnderecos() {
        return this.enderecos;
    }


    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }


    public abstract String getDocumento();
    public abstract void setDocumento(String documento);
}