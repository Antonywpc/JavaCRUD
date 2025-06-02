package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {

    private Map<Integer, List<MenuItem>> menuMap = new HashMap<>();
    private Scanner input;

    public Menu() {
        this.input = new Scanner(System.in);
        carregarMenu();
    }

    private void carregarMenu() {
        try (Scanner scanner = new Scanner(new File("menu.txt"))) {
            while (scanner.hasNextLine()) {
                String linha = scanner.nextLine();
                if (linha.startsWith("#") || linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                int nivel = Integer.parseInt(partes[0]);
                int pai = Integer.parseInt(partes[1]);
                int id = Integer.parseInt(partes[2]);
                String nome = partes[3];
                MenuItem item = new MenuItem(nivel, pai, id, nome);
                menuMap.computeIfAbsent(pai, k -> new ArrayList<>()).add(item);
            }
        } catch (Exception e) {
            System.out.println("Erro crítico ao carregar o menu: " + e.getMessage());
        }
    }

    public int exibirMenu(int pai, String titulo) {
        System.out.println("\n--- " + titulo.toUpperCase() + " ---");
        List<MenuItem> opcoes = menuMap.getOrDefault(pai, new ArrayList<>());
        if (opcoes.isEmpty()) {
            System.out.println("Submenu não encontrado ou vazio.");
            return -1;
        }

        for (MenuItem item : opcoes) {
            System.out.println(item.id + " - " + item.nome);
        }
        System.out.println("Qualquer outra tecla para Voltar.");
        System.out.print("Escolha uma opção: ");

        int escolha;
        try {
            escolha = input.nextInt();
        } catch (InputMismatchException e) {
            escolha = -1; // Se digitar texto, considera "voltar"
        }
        input.nextLine(); // Limpa o buffer
        return escolha;
    }

    // Método para pegar a entrada do usuário (delegando o Scanner)
    public String lerEntradaString(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    public int lerEntradaInt(String prompt) {
        System.out.print(prompt);
        int valor = input.nextInt();
        input.nextLine(); // Limpa o buffer
        return valor;
    }
    
    public double lerEntradaDouble(String prompt) {
        System.out.print(prompt);
        double valor = input.nextDouble();
        input.nextLine(); // Limpa o buffer
        return valor;
    }
}