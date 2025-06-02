package app;

import modelo.*;
import ui.Menu;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Sistema {
    private List<Pessoa> pessoas = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    private Menu menu;

    public Sistema() {
        this.menu = new Menu();
    }

    public void executar() {
        System.out.println("=== SISTEMA DE CADASTRO INICIADO ===");
        boolean executando = true;
        while (executando) {
            int escolha = menu.exibirMenu(0, "MENU PRINCIPAL");
            switch (escolha) {
                case 1: menuPessoas(); break;
                case 2: menuProdutos(); break;
                case 3:
                    System.out.println("Encerrando o sistema...");
                    executando = false;
                    break;
                default: break;
            }
        }
    }

    private void menuPessoas() {
        boolean voltando = false;
        while (!voltando) {
            int escolha = menu.exibirMenu(1, "GESTÃO DE PESSOAS");
            switch (escolha) {
                case 1: incluirPessoa(); break;
                case 2: listarPessoas(); break;
                case 3: alterarPessoa(); break;
                case 4: excluirPessoa(); break;
                default: voltando = true;
            }
        }
    }

    private void menuProdutos() {
        boolean voltando = false;
        while (!voltando) {
            int escolha = menu.exibirMenu(2, "GESTÃO DE PRODUTOS");
            switch (escolha) {
                case 1: incluirProduto(); break;
                case 2: listarProdutos(); break;
                case 3: alterarProduto(); break;
                case 4: excluirProduto(); break;
                default: voltando = true;
            }
        }
    }

    // --- MÉTODOS DE PESSOA (CRUD) ---
    private void alterarPessoa() {
        int id = menu.lerEntradaInt("Digite o ID da pessoa a alterar: ");
        Pessoa pessoa = buscarPessoaPorId(id);

        if (pessoa == null || !pessoa.isAtivo()) {
            System.out.println("Pessoa não encontrada ou inativa.");
            return;
        }

        boolean alterando = true;
        while(alterando) {
            System.out.println("\nAlterando: " + pessoa.getNome() + " (" + pessoa.getDocumento() + ")");
            System.out.println("1 - Alterar Nome");
            System.out.println("2 - Alterar Documento (CPF/CNPJ)");
            System.out.println("3 - Adicionar Endereço");
            System.out.println("4 - Remover Endereço");
            System.out.println("5 - Voltar");
            int escolha = menu.lerEntradaInt("O que deseja fazer? ");

            switch(escolha) {
                case 1:
                    String novoNome = menu.lerEntradaString("Novo nome: ");
                    pessoa.setNome(novoNome);
                    gerarLog("Pessoa ID " + pessoa.getId() + " teve o nome alterado para " + novoNome);
                    System.out.println("Nome alterado com sucesso!");
                    break;
                case 2:
                    alterarDocumentoPessoa(pessoa);
                    break;
                case 3:
                    cadastrarEnderecoParaPessoa(pessoa);
                    break;
                case 4:
                    removerEnderecoDePessoa(pessoa);
                    break;
                case 5:
                    alterando = false;
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void alterarDocumentoPessoa(Pessoa pessoa) {
        if (pessoa instanceof Cliente) {
            String novoCpf;
            do {
                novoCpf = menu.lerEntradaString("Digite o novo CPF (11 dígitos): ");
            } while (!novoCpf.matches("\\d{11}"));
            pessoa.setDocumento(novoCpf);
        } else if (pessoa instanceof Fornecedor) {
            String novoCnpj;
            do {
                novoCnpj = menu.lerEntradaString("Digite o novo CNPJ (14 dígitos): ");
            } while (!novoCnpj.matches("\\d{14}"));
            pessoa.setDocumento(novoCnpj);
        }
        gerarLog("Documento da Pessoa ID " + pessoa.getId() + " foi alterado.");
        System.out.println("Documento alterado com sucesso!");
    }
    
    private void incluirPessoa() { 
        System.out.println("\n--- CADASTRO DE PESSOA ---");
        String tipoPessoa;
        while (true) {
            tipoPessoa = menu.lerEntradaString("Digite o tipo (Cliente ou Fornecedor): ");
            if (tipoPessoa.equalsIgnoreCase("cliente") || tipoPessoa.equalsIgnoreCase("fornecedor")) {
                break;
            }
            System.out.println("Tipo inválido. Por favor, digite 'Cliente' ou 'Fornecedor'.");
        }

        String nome = menu.lerEntradaString("Nome: ");
        Pessoa pessoa;

        if (tipoPessoa.equalsIgnoreCase("cliente")) {
            String cpf;
            do {
                cpf = menu.lerEntradaString("CPF (11 dígitos, somente números): ");
            } while (!cpf.matches("\\d{11}"));
            pessoa = new Cliente(nome, cpf);
        } else {
            String cnpj;
            do {
                cnpj = menu.lerEntradaString("CNPJ (14 dígitos, somente números): ");
            } while (!cnpj.matches("\\d{14}"));
            pessoa = new Fornecedor(nome, cnpj);
        }
        pessoas.add(pessoa);
        gerarLog("Pessoa cadastrada: ID " + pessoa.getId() + " - " + pessoa.getNome());
        System.out.println("Cadastro de pessoa efetuado com sucesso! ID: " + pessoa.getId());

        while (true) {
            String resposta = menu.lerEntradaString("\nDeseja cadastrar um endereço para esta pessoa? (s/n): ");
            if (resposta.equalsIgnoreCase("s")) {
                cadastrarEnderecoParaPessoa(pessoa);
            } else {
                break;
            }
        }
    }
    
    private void cadastrarEnderecoParaPessoa(Pessoa pessoa) {
        System.out.println("\n--- CADASTRANDO ENDEREÇO PARA: " + pessoa.getNome() + " ---");
        String tipo = menu.lerEntradaString("Tipo de Endereço (ex: Residencial, Comercial, Entrega): ");
        String cep = menu.lerEntradaString("CEP: ");
        String logradouro = menu.lerEntradaString("Logradouro (Rua/Av): ");
        String numero = menu.lerEntradaString("Número: ");
        String complemento = menu.lerEntradaString("Complemento: ");

        Endereco endereco = new Endereco(cep, logradouro, numero, complemento, tipo);
        pessoa.adicionarEndereco(endereco);

        gerarLog("Endereço cadastrado para a pessoa ID " + pessoa.getId() + ": " + endereco);
        System.out.println("Endereço cadastrado com sucesso!");
    }

    private void listarPessoas() {
        System.out.println("\n--- LISTA DE PESSOAS ATIVAS ---");
        if (pessoas.stream().noneMatch(Pessoa::isAtivo)) {
            System.out.println("Nenhuma pessoa ativa cadastrada.");
            return;
        }

        for (Pessoa p : pessoas) {
            if (p.isAtivo()) {
                String tipo = (p instanceof Cliente) ? "Cliente" : "Fornecedor";
                System.out.println("----------------------------------------");
                System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() +
                                   " | Documento: " + p.getDocumento() + " | Tipo: " + tipo);
                
                if (!p.getEnderecos().isEmpty()) {
                    System.out.println("  Endereços:");
                    for (Endereco end : p.getEnderecos()) {
                        System.out.println("  -> " + end.toString());
                    }
                } else {
                    System.out.println("  (Sem endereços cadastrados)");
                }
            }
        }
        System.out.println("----------------------------------------");
    }
    
    private void removerEnderecoDePessoa(Pessoa pessoa) {
        if (pessoa.getEnderecos().isEmpty()) {
            System.out.println("Esta pessoa não possui endereços para remover.");
            return;
        }
        System.out.println("Qual endereço você deseja remover?");
        List<Endereco> enderecos = pessoa.getEnderecos();
        for (int i = 0; i < enderecos.size(); i++) {
            System.out.println((i + 1) + " - " + enderecos.get(i));
        }
        int indice = menu.lerEntradaInt("Digite o número do endereço: ") - 1;

        if (indice >= 0 && indice < enderecos.size()) {
            Endereco endRemovido = enderecos.remove(indice);
            gerarLog("Endereço removido da pessoa ID " + pessoa.getId() + ": " + endRemovido);
            System.out.println("Endereço removido com sucesso.");
        } else {
            System.out.println("Número inválido.");
        }
    }

    private void excluirPessoa() {
        int id = menu.lerEntradaInt("Digite o ID da pessoa a excluir (desativar): ");
        Pessoa pessoa = buscarPessoaPorId(id);

        if (pessoa != null && pessoa.isAtivo()) {
            pessoa.setAtivo(false);
            gerarLog("Pessoa desativada: ID " + pessoa.getId());
            System.out.println("Pessoa desativada com sucesso.");
        } else {
            System.out.println("Pessoa não encontrada ou já inativa.");
        }
    }
    
    // --- MÉTODOS DE PRODUTO (CRUD) - CÓDIGO RESTAURADO ---
    
    private void incluirProduto() {
        System.out.println("\n--- INCLUIR PRODUTO ---");
        String descricao = menu.lerEntradaString("Descrição: ");
        double custo = menu.lerEntradaDouble("Custo (R$): ");
        double precoVenda = menu.lerEntradaDouble("Preço de Venda (R$): ");
        int idFornecedor = menu.lerEntradaInt("Digite o ID do Fornecedor: ");
        
        Pessoa fornecedor = buscarPessoaPorId(idFornecedor);
        if (fornecedor == null || !(fornecedor instanceof Fornecedor) || !fornecedor.isAtivo()) {
            System.out.println("Erro: Fornecedor com ID " + idFornecedor + " não encontrado, inativo ou não é um fornecedor.");
            return;
        }

        Produto produto = new Produto(descricao, custo, precoVenda, idFornecedor);
        produtos.add(produto);
        gerarLog("Produto incluído: Cód " + produto.getCodigo() + " - " + produto.getDescricao());
        System.out.println("Produto cadastrado com sucesso! Código: " + produto.getCodigo());
    }

    private void listarProdutos() {
        System.out.println("\n--- LISTA DE PRODUTOS ATIVOS ---");
        if (produtos.stream().noneMatch(Produto::isAtivo)) {
            System.out.println("Nenhum produto ativo cadastrado.");
            return;
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Produto p : produtos) {
            if (p.isAtivo()) {
                System.out.printf("Cód: %-4d | Descrição: %-30s | Custo: R$ %-8.2f | Preço Venda: R$ %-8.2f | Forn. ID: %d\n",
                    p.getCodigo(), p.getDescricao(), p.getCusto(), p.getPrecoVenda(), p.getIdFornecedor());
            }
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    private void alterarProduto() {
        int codigo = menu.lerEntradaInt("Digite o código do produto a alterar: ");
        
        Produto produto = null;
        for (Produto p : produtos) {
            if (p.getCodigo() == codigo && p.isAtivo()) {
                produto = p;
                break;
            }
        }

        if (produto != null) {
            System.out.println("Alterando produto: " + produto.getDescricao());
            
            String novaDesc = menu.lerEntradaString("Nova descrição (deixe em branco para manter): ");
            if (!novaDesc.trim().isEmpty()) {
                produto.setDescricao(novaDesc);
            }
            
            String novoCustoStr = menu.lerEntradaString("Novo preço de CUSTO (deixe em branco para manter): R$ ");
            if (!novoCustoStr.trim().isEmpty()) {
                try {
                    double novoCusto = Double.parseDouble(novoCustoStr);
                    produto.setCusto(novoCusto);
                } catch(NumberFormatException e) {
                    System.out.println("Valor de custo inválido. Mantendo o anterior.");
                }
            }
            
            String novoPrecoStr = menu.lerEntradaString("Novo preço de VENDA (deixe em branco para manter): R$ ");
            if (!novoPrecoStr.trim().isEmpty()) {
                try {
                    double novoPreco = Double.parseDouble(novoPrecoStr);
                    produto.setPrecoVenda(novoPreco);
                } catch(NumberFormatException e) {
                    System.out.println("Valor de venda inválido. Mantendo o anterior.");
                }
            }
            
            gerarLog("Produto alterado: Cód " + produto.getCodigo());
            System.out.println("Produto alterado com sucesso!");
        } else {
            System.out.println("Produto não encontrado ou inativo.");
        }
    }
    
    private void excluirProduto() {
        int codigo = menu.lerEntradaInt("Digite o código do produto a excluir (desativar): ");

        for (Produto p : produtos) {
            if (p.getCodigo() == codigo && p.isAtivo()) {
                p.setAtivo(false);
                gerarLog("Produto desativado: Cód " + p.getCodigo());
                System.out.println("Produto desativado com sucesso.");
                return;
            }
        }
        System.out.println("Produto não encontrado ou já inativo.");
    }

    // --- MÉTODOS UTILITÁRIOS ---
    
    private Pessoa buscarPessoaPorId(int id) {
        for (Pessoa p : pessoas) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }
    
    private void gerarLog(String mensagem) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataFormatada = sdf.format(new Date());
        
        try (FileWriter fw = new FileWriter("log.txt", true)) {
            fw.write(dataFormatada + " - " + mensagem + "\n");
        } catch (IOException e) {
            System.out.println("Erro ao gerar log: " + e.getMessage());
        }
    }
}