package modelo;

import java.util.ArrayList;
import java.util.List;

// Classe abstrata base para Cliente e Fornecedor
public abstract class Pessoa {
    private static int contador = 1;
    private int id;
    private String nome;
    private boolean ativo;
    private List<Endereco> enderecos; // <-- ADICIONADO

    public Pessoa(String nome) {
        this.id = contador++;
        this.nome = nome;
        this.ativo = true;
        this.enderecos = new ArrayList<>(); // <-- ADICIONADO: Inicializa a lista
    }

    // --- MÉTODOS NOVOS PARA ENDEREÇO ---
    public void adicionarEndereco(Endereco endereco) { // <-- ADICIONADO
        this.enderecos.add(endereco);
    }

    public List<Endereco> getEnderecos() { // <-- ADICIONADO
        return this.enderecos;
    }
    // ------------------------------------

    // Getters e Setters existentes
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }

    // Método abstrato para ser implementado pelas classes filhas
    public abstract String getDocumento();
    public abstract void setDocumento(String documento);
}