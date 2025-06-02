package ui;

public class MenuItem {
    int nivel;
    int pai;
    int id;
    String nome;

    public MenuItem(int nivel, int pai, int id, String nome) {
        this.nivel = nivel;
        this.pai = pai;
        this.id = id;
        this.nome = nome;
    }
}