package ui;

import java.io.File;
import java.io.IOException;
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
                if (linha.startsWith("#") || linha.trim().isEmpty())
                    continue;
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
            escolha = -1;
        }
        input.nextLine();
        return escolha;
    }

    public String lerEntradaString(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    public int lerEntradaInt(String prompt) {
        System.out.print(prompt);
        int valor = input.nextInt();
        input.nextLine();
        return valor;
    }

    public int lerEntradaIntVerificado(String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = input.nextLine();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite apenas números inteiros.");
            }
        }
    }

    public double lerEntradaDouble(String prompt) {
        System.out.print(prompt);
        double valor = input.nextDouble();
        input.nextLine();
        return valor;
    }

    public double lerEntradaDoubleVerificado(String prompt) {
        while (true) {
            System.out.print(prompt);
            String entrada = input.nextLine();

            try {
                return Double.parseDouble(entrada.replace(",", ".")); // Suporta vírgula também
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número válido com ou sem casas decimais.");
            }
        }
    }

    public void limparTela() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                // Comando para limpar o console do Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                // Este comando (ANSI escape code) funciona na maioria dos terminais
                // modernos (Linux, macOS) e também nos terminais de IDEs como o VS Code.
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (final IOException | InterruptedException e) {
            // Se a limpeza falhar por algum motivo, o programa não vai quebrar.
            // Podemos imprimir algumas linhas em branco como uma alternativa simples.
            for (int i = 0; i < 10; i++) {
                System.out.println();
            }
        }
    }

    public void esperarEnter() {
        System.out.print("\nPressione Enter para continuar...");
        input.nextLine(); // Esta chamada simplesmente consome a próxima linha (o Enter)
    }

}