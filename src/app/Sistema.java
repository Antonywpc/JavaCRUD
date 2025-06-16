package app;

import modelo.*;
import ui.Menu;
import validadores.*;
import modelo.enums.*;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Sistema {
    private List<Pessoa> pessoas = new ArrayList<>();
    private List<Produto> produtos = new ArrayList<>();
    private List<PedidoVenda> pedidos = new ArrayList<>();

    private Menu menu;

    public Sistema() {
        this.menu = new Menu();
    }

    // --- MÉTODO PRINCIPAL ---

    public void executar() {
        boolean executando = true;
        while (executando) {
            menu.limparTela();
            int escolha = menu.exibirMenu(0, "MENU PRINCIPAL");
            switch (escolha) {
                case 1:
                    menuPessoas();
                    break;
                case 2:
                    menuProdutos();
                    break;
                case 3:
                    menuPedidos();
                    break;
                case 4:
                    System.out.println("Encerrando o sistema...");
                    executando = false;
                    break;
                default:
                    break;
            }
        }
    }

    // --- NAVEGAÇÃO DOS MENUS ---

    private void menuPessoas() {
        boolean voltando = false;
        while (!voltando) {
            menu.limparTela();
            int escolha = menu.exibirMenu(1, "GESTÃO DE PESSOAS");

            switch (escolha) {
                case 1:
                    incluirPessoa();
                    break;
                case 2:
                    listarPessoas();
                    break;
                case 3:
                    alterarPessoa();
                    break;
                case 4:
                    excluirPessoa();
                    break;
                default:
                    voltando = true;
                    continue;
            }
            menu.esperarEnter();
        }
    }

    private void menuProdutos() {
        boolean voltando = false;
        while (!voltando) {
            menu.limparTela();
            int escolha = menu.exibirMenu(2, "GESTÃO DE PRODUTOS");

            switch (escolha) {
                case 1:
                    incluirProduto();
                    break;
                case 2:
                    listarProdutos();
                    break;
                case 3:
                    alterarProduto();
                    break;
                case 4:
                    excluirProduto();
                    break;
                default:
                    voltando = true;
                    continue;
            }
            menu.esperarEnter();
        }
    }

    private void menuPedidos() {
        boolean voltando = false;
        while (!voltando) {
            menu.limparTela();
            int escolha = menu.exibirMenu(3, "GESTÃO DE PEDIDOS");

            switch (escolha) {
                case 1:
                    incluirPedido();
                    break;
                case 2:
                    listarPedidos();
                    break;
                case 3:
                    alterarPedido();
                    break;
                case 4:
                    excluirPedido();
                    break;
                default:
                    voltando = true;
                    continue;
            }
            menu.esperarEnter();
        }
    }

    // --- MÓDULO DE GESTÃO DE PESSOAS ---

    private void incluirPessoa() {
        menu.limparTela();
        System.out.println("\n--- CADASTRO DE PESSOA ---");
        String tipoPessoa;
        while (true) {
            tipoPessoa = menu.lerEntradaString("Digite o tipo (Cliente ou Fornecedor): ");
            if (tipoPessoa.equalsIgnoreCase("cliente") || tipoPessoa.equalsIgnoreCase("fornecedor")) {
                break;
            }
            System.out.println("Tipo inválido. Por favor, digite 'Cliente' ou 'Fornecedor'.");
        }

        String nome;
        while (true) {
            nome = menu.lerEntradaString("Nome: ");
            if (!nome.trim().isEmpty()) {
                break;
            } else {
                System.out.println("Nome não pode ser vazio. Tente novamente.");
            }
        }

        Pessoa pessoa;
        if (tipoPessoa.equalsIgnoreCase("cliente")) {
            String cpf;
            do {
                cpf = menu.lerEntradaString("CPF (somente números): ");
            } while (!ValidacaoCPF.validar(cpf));
            pessoa = new Cliente(nome, cpf);
        } else {
            String cnpj;
            do {
                cnpj = menu.lerEntradaString("CNPJ (somente números): ");
            } while (!ValidacaoCNPJ.validar(cnpj));
            pessoa = new Fornecedor(nome, cnpj);
        }
        pessoas.add(pessoa);
        gerarLog("Pessoa cadastrada: ID " + pessoa.getId() + " - " + pessoa.getNome());
        System.out.println("Cadastro de pessoa efetuado com sucesso! ID: " + pessoa.getId());

        boolean primeiroCadastro = true;
        while (true) {
            String pergunta = primeiroCadastro ? "\nDeseja cadastrar um endereço para esta pessoa? (s/n): "
                    : "\nDeseja cadastrar mais um endereço para esta pessoa? (s/n): ";
            String resposta = menu.lerEntradaString(pergunta);

            if (!resposta.equalsIgnoreCase("s") && !resposta.equalsIgnoreCase("n")) {
                System.out.println("Opção inválida. Digite 's' para sim ou 'n' para não.");
                continue;
            }
            if (resposta.equalsIgnoreCase("s")) {
                cadastrarEnderecoParaPessoa(pessoa);
                primeiroCadastro = false;
            } else {
                break;
            }
        }
    }

    private void listarPessoas() {
        menu.limparTela();
        System.out.println("\n--- LISTA DE PESSOAS ATIVAS ---");
        if (pessoas.stream().noneMatch(Pessoa::isAtivo)) {
            System.out.println("Nenhuma pessoa ativa cadastrada.");
            return;
        }

        for (Pessoa p : pessoas) {
            if (p.isAtivo()) {
                String tipo = (p instanceof Cliente) ? "Cliente" : "Fornecedor";
                System.out.println("----------------------------------------");
                System.out.println("ID: " + p.getId() + " | Nome: " + p.getNome() + " | Documento: " + p.getDocumento()
                        + " | Tipo: " + tipo);
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

    private void alterarPessoa() {
        menu.limparTela();
        System.out.println("\n--- ALTERAR PESSOA ---");
        List<Pessoa> pessoasAtivas = pessoas.stream().filter(Pessoa::isAtivo).collect(Collectors.toList());
        if (pessoasAtivas.isEmpty()) {
            System.out.println("Nenhuma pessoa ativa encontrada para alterar.");
            return;
        }
        System.out.println("\nPessoas ativas no sistema:");
        for (Pessoa p : pessoasAtivas) {
            String tipo = (p instanceof Cliente) ? "Cliente" : "Fornecedor";
            System.out.printf("ID: %d | Nome: %s | Tipo: %s\n", p.getId(), p.getNome(), tipo);
        }
        int id = menu.lerEntradaIntVerificado("\nDigite o ID da pessoa que deseja alterar: ");
        Pessoa pessoa = buscarPessoaPorId(id);
        if (pessoa == null || !pessoa.isAtivo()) {
            System.out.println("ID não encontrado entre as pessoas ativas.");
            return;
        }

        boolean alterando = true;
        while (alterando) {
            menu.limparTela();
            System.out.println("\nAlterando: " + pessoa.getNome() + " (" + pessoa.getDocumento() + ")");
            System.out.println("1 - Alterar Nome");
            System.out.println("2 - Alterar Documento (CPF/CNPJ)");
            System.out.println("3 - Adicionar Endereço");
            System.out.println("4 - Remover Endereço");
            System.out.println("5 - Voltar");
            int escolha = menu.lerEntradaIntVerificado("O que deseja fazer? ");

            switch (escolha) {
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

    private void excluirPessoa() {
        int id = menu.lerEntradaIntVerificado("Digite o ID da pessoa a excluir (desativar): ");
        Pessoa pessoa = buscarPessoaPorId(id);

        if (pessoa != null && pessoa.isAtivo()) {
            pessoa.setAtivo(false);
            gerarLog("Pessoa desativada: ID " + pessoa.getId());
            System.out.println("Pessoa desativada com sucesso.");
        } else {
            System.out.println("Pessoa não encontrada ou já inativa.");
        }
    }

    private void alterarDocumentoPessoa(Pessoa pessoa) {
        if (pessoa instanceof Cliente) {
            String novoCpf;
            do {
                novoCpf = menu.lerEntradaString("Digite o novo CPF (11 dígitos): ");
            } while (!ValidacaoCPF.validar(novoCpf));
            pessoa.setDocumento(novoCpf);
        } else if (pessoa instanceof Fornecedor) {
            String novoCnpj;
            do {
                novoCnpj = menu.lerEntradaString("Digite o novo CNPJ (14 dígitos): ");
            } while (!ValidacaoCNPJ.validar(novoCnpj));
            pessoa.setDocumento(novoCnpj);
        }
        gerarLog("Documento da Pessoa ID " + pessoa.getId() + " foi alterado.");
        System.out.println("Documento alterado com sucesso!");
    }

    private void cadastrarEnderecoParaPessoa(Pessoa pessoa) {
        System.out.println("\n--- CADASTRANDO ENDEREÇO PARA: " + pessoa.getNome() + " ---");
        TipoEndereco tipoEndereco = null;
        while (tipoEndereco == null) {
            String entradaTipo = menu.lerEntradaString(
                    "Tipo de Endereço (Residencial, Comercial, Fiscal, Entrega ou Correspondência): ");
            tipoEndereco = TipoEndereco.fromDescricao(entradaTipo);
            if (tipoEndereco == null) {
                System.out.println("Tipo inválido. Digite um dos seguintes:");
                TipoEndereco.listarTipos();
            }
        }
        String cep;
        do {
            cep = menu.lerEntradaString("CEP (somente números): ");
        } while (!ValidacaoCEP.validar(cep));

        String logradouro = menu.lerEntradaString("Logradouro (Rua/Av): ");
        String numero;
        do {
            numero = menu.lerEntradaString("Número: ");
        } while (!numero.matches("\\d+"));

        String complemento = menu.lerEntradaString("Complemento: ");
        Endereco endereco = new Endereco(cep, logradouro, numero, complemento, tipoEndereco);
        pessoa.adicionarEndereco(endereco);
        gerarLog("Endereço cadastrado para a pessoa ID " + pessoa.getId() + ": " + endereco);
        System.out.println("Endereço cadastrado com sucesso!");
    }

    private void removerEnderecoDePessoa(Pessoa pessoa) {
        if (pessoa.getEnderecos().isEmpty()) {
            System.out.println("Esta pessoa não possui endereços para remover.");
            return;
        }
        System.out.println("\nQual endereço você deseja remover?");
        List<Endereco> enderecos = pessoa.getEnderecos();
        for (int i = 0; i < enderecos.size(); i++) {
            System.out.println((i + 1) + " - " + enderecos.get(i));
        }
        
        int indice = menu.lerEntradaIntVerificado("Digite o número do endereço: ") - 1;

        if (indice >= 0 && indice < enderecos.size()) {
            Endereco endRemovido = enderecos.remove(indice);
            gerarLog("Endereço removido da pessoa ID " + pessoa.getId() + ": " + endRemovido);
            System.out.println("Endereço removido com sucesso.");
        } else {
            System.out.println("Número inválido.");
        }
    }
    // --- MÓDULO DE GESTÃO DE PRODUTOS ---

  private void incluirProduto() {
    menu.limparTela();
        System.out.println("\n--- INCLUIR PRODUTO ---");
        
        Pessoa fornecedor = selecionarFornecedorParaProduto();
        if (fornecedor == null) {
            System.out.println("\nInclusão de produto cancelada, pois nenhum fornecedor foi selecionado.");
            return;
        }

        System.out.println("\nFornecedor selecionado: " + fornecedor.getNome());
        String descricao = menu.lerEntradaString("Descrição do produto: ");
        double custo = menu.lerEntradaDoubleVerificado("Custo (R$): ");
        double precoVenda = menu.lerEntradaDoubleVerificado("Preço de Venda (R$): ");
        
        Produto produto = new Produto(descricao, custo, precoVenda, fornecedor.getId());
        produtos.add(produto);
        gerarLog("Produto incluído: Cód " + produto.getCodigo() + " - " + produto.getDescricao());
        System.out.println("Produto cadastrado com sucesso! Código: " + produto.getCodigo());
    }

    private Pessoa selecionarFornecedorParaProduto() {
        System.out.println("\nSelecione o Fornecedor do produto:");
        List<Pessoa> fornecedoresAtivos = pessoas.stream()
            .filter(p -> p instanceof Fornecedor && p.isAtivo())
            .collect(Collectors.toList());

        if (fornecedoresAtivos.isEmpty()) {
            System.out.println("Nenhum fornecedor ativo encontrado. Cadastre um fornecedor primeiro.");
            return null;
        }

        for (int i = 0; i < fornecedoresAtivos.size(); i++) {
            Pessoa fornecedor = fornecedoresAtivos.get(i);
            System.out.printf("%d - %s (ID: %d)\n", (i + 1), fornecedor.getNome(), fornecedor.getId());
        }

        int escolha = menu.lerEntradaIntVerificado("Escolha o número do fornecedor: ");
        if (escolha > 0 && escolha <= fornecedoresAtivos.size()) {
            return fornecedoresAtivos.get(escolha - 1);
        } else {
            System.out.println("Seleção inválida.");
            return null;
        }
    }

    private void listarProdutos() {
        menu.limparTela();
        System.out.println("\n--- LISTA DE PRODUTOS ATIVOS ---");
        if (produtos.stream().noneMatch(Produto::isAtivo)) {
            System.out.println("Nenhum produto ativo cadastrado.");
            return;
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        for (Produto p : produtos) {
            if (p.isAtivo()) {
                System.out.printf(
                        "Cód: %-4d | Descrição: %-30s | Custo: R$ %-8.2f | Preço Venda: R$ %-8.2f | Forn. ID: %d\n",
                        p.getCodigo(), p.getDescricao(), p.getCusto(), p.getPrecoVenda(), p.getIdFornecedor());
            }
        }
        System.out.println("-----------------------------------------------------------------------------------------");
    }

    private void alterarProduto() {
        menu.limparTela();
        System.out.println("\n--- ALTERAR PRODUTO ---");
        List<Produto> produtosAtivos = produtos.stream().filter(Produto::isAtivo).collect(Collectors.toList());

        if (produtosAtivos.isEmpty()) {
            System.out.println("Nenhum produto ativo encontrado para alterar.");
            return;
        }

        System.out.println("\nProdutos ativos no sistema:");
        for (Produto p : produtosAtivos) {
            System.out.printf("Cód: %d | Descrição: %s\n", p.getCodigo(), p.getDescricao());
        }
        
        int codigo = menu.lerEntradaIntVerificado("\nDigite o código do produto a alterar: ");
        Produto produto = buscarProdutoPorCodigo(codigo);
        
        if (produto == null || !produto.isAtivo()) {
            System.out.println("Produto não encontrado ou inativo.");
            return;
        }

        boolean alterando = true;
        while (alterando) {
            menu.limparTela();
            System.out.println("\nAlterando Produto: " + produto.getDescricao());
            System.out.println("1 - Alterar Descrição");
            System.out.println("2 - Alterar Preço de Custo");
            System.out.println("3 - Alterar Preço de Venda");
            System.out.println("4 - Concluir Alterações");
            int escolha = menu.lerEntradaIntVerificado("O que deseja fazer? ");

            switch (escolha) {
                case 1:
                    String novaDesc = menu.lerEntradaString("Nova descrição: ");
                    produto.setDescricao(novaDesc);
                    System.out.println("Descrição alterada com sucesso!");
                    break;
                case 2:
                    double novoCusto = menu.lerEntradaDoubleVerificado("Novo preço de CUSTO: R$ ");
                    produto.setCusto(novoCusto);
                    System.out.println("Preço de custo alterado com sucesso!");
                    break;
                case 3:
                    double novoPreco = menu.lerEntradaDoubleVerificado("Novo preço de VENDA: R$ ");
                    produto.setPrecoVenda(novoPreco);
                    System.out.println("Preço de venda alterado com sucesso!");
                    break;
                case 4:
                    alterando = false;
                    break;
                default:
                    menu.limparTela();
                    System.out.println("Opção inválida.");
            }
        }
        gerarLog("Produto alterado: Cód " + produto.getCodigo());
        System.out.println("Alterações no produto concluídas.");
    }

    private void excluirProduto() {
        menu.limparTela();
        int codigo = menu.lerEntradaIntVerificado("Digite o código do produto a excluir (desativar): ");
        Produto produto = buscarProdutoPorCodigo(codigo);

        if (produto != null && produto.isAtivo()) {
            produto.setAtivo(false);
            gerarLog("Produto desativado: Cód " + produto.getCodigo());
            System.out.println("Produto desativado com sucesso.");
        } else {
            System.out.println("Produto não encontrado ou já inativo.");
        }
    }

    // --- MÓDULO DE GESTÃO DE PEDIDOS ---

    private void incluirPedido() {
        menu.limparTela();
        System.out.println("\n--- INCLUIR NOVO PEDIDO DE VENDA ---");
        Pessoa cliente = selecionarClienteParaPedido();
        if (cliente == null) {
            return;
        }
        TipoVenda tipoVenda = selecionarTipoVenda();
        Endereco enderecoEntrega = null;
        if (tipoVenda == TipoVenda.ENTREGA) {
            enderecoEntrega = selecionarEnderecoParaPedido(cliente);
            if (enderecoEntrega == null) {
                System.out.println("Como não foi selecionado um endereço, a criação do pedido foi cancelada.");
                return;
            }
        }
        PedidoVenda novoPedido = new PedidoVenda(cliente.getId(), tipoVenda, enderecoEntrega);
        adicionarItensAoPedido(novoPedido);
        if (novoPedido.getItens().isEmpty()) {
            System.out.println("\nPedido cancelado, pois nenhum item foi adicionado.");
        } else {
            pedidos.add(novoPedido);
            gerarLog("Pedido de Venda incluído: Nº " + novoPedido.getNumeroPedido() + " para o cliente ID "
                    + novoPedido.getIdCliente());
            System.out.println("\nPedido Nº " + novoPedido.getNumeroPedido() + " registrado com sucesso!");
        }
    }

    private void listarPedidos() {
        menu.limparTela();
        System.out.println("\n--- LISTA DE PEDIDOS DE VENDA ---");
        List<PedidoVenda> pedidosAtivos = pedidos.stream().filter(PedidoVenda::isAtivo).collect(Collectors.toList());
        if (pedidosAtivos.isEmpty()) {
            System.out.println("Nenhum pedido de venda ativo registrado.");
            return;
        }

        for (PedidoVenda pedido : pedidosAtivos) {
            Pessoa cliente = buscarPessoaPorId(pedido.getIdCliente());
            String nomeCliente = (cliente != null) ? cliente.getNome() : "Cliente não encontrado";
            System.out.println("==============================================");
            System.out.printf("Pedido Nº: %d | Cliente: %s (ID: %d)\n", pedido.getNumeroPedido(), nomeCliente,
                    pedido.getIdCliente());
            System.out.printf("Tipo de Venda: %s\n", pedido.getTipoVenda().getDescricao());
            if (pedido.getTipoVenda() == TipoVenda.ENTREGA && pedido.getEnderecoEntrega() != null) {
                System.out.printf("Endereço de Entrega: %s\n", pedido.getEnderecoEntrega());
            }
            System.out.println("--- Itens do Pedido ---");
            for (ItemVenda item : pedido.getItens()) {
                Produto produto = buscarProdutoPorCodigo(item.getIdProduto());
                String descProduto = (produto != null) ? produto.getDescricao() : "Produto não encontrado";
                System.out.printf("  - Produto: %-30s | Qtd: %d | Preço Unit.: R$ %.2f | Subtotal: R$ %.2f\n",
                        descProduto, item.getQuantidade(), item.getPrecoUnitario(), item.getValorTotalItem());
            }
            System.out.println("----------------------------------------------");
            System.out.printf("VALOR TOTAL DO PEDIDO: R$ %.2f\n", pedido.getTotalPedido());
            System.out.println("==============================================");
        }
    }

    private void alterarPedido() {
        menu.limparTela();
        System.out.println("\n--- ALTERAR PEDIDO DE VENDA ---");
        List<PedidoVenda> pedidosAtivos = pedidos.stream().filter(PedidoVenda::isAtivo).collect(Collectors.toList());
        if (pedidosAtivos.isEmpty()) {
            System.out.println("Nenhum pedido ativo para alterar.");
            return;
        }
        System.out.println("\nSelecione o pedido a ser alterado:");
        for (PedidoVenda pedido : pedidosAtivos) {
            Pessoa cliente = buscarPessoaPorId(pedido.getIdCliente());
            String nomeCliente = (cliente != null) ? cliente.getNome() : "Cliente não encontrado";
            System.out.printf("Nº: %d | Cliente: %s | Total: R$ %.2f\n", pedido.getNumeroPedido(), nomeCliente,
                    pedido.getTotalPedido());
        }
        int numeroPedido = menu.lerEntradaIntVerificado("\nDigite o número do pedido: ");
        PedidoVenda pedidoParaAlterar = buscarPedidoPorNumero(numeroPedido);
        if (pedidoParaAlterar == null || !pedidoParaAlterar.isAtivo()) {
            System.out.println("Pedido não encontrado ou está inativo.");
            return;
        }
        boolean alterando = true;
        while (alterando) {

            System.out.println("\nAlterando Pedido Nº: " + pedidoParaAlterar.getNumeroPedido());
            System.out.println("1 - Adicionar Novo Item");
            System.out.println("2 - Remover Item Existente");
            System.out.println("3 - Concluir Alterações");
            int escolha = menu.lerEntradaIntVerificado("O que deseja fazer? ");
            switch (escolha) {
                case 1:
                    adicionarItensAoPedido(pedidoParaAlterar);
                    break;
                case 2:
                    removerItemDePedido(pedidoParaAlterar);
                    break;
                case 3:
                    alterando = false;
                    break;
                default:
                    menu.limparTela();
                    System.out.println("Opção inválida.");
            }
            pedidoParaAlterar.recalcularTotalPedido();
        }
        gerarLog("Pedido de Venda alterado: Nº " + pedidoParaAlterar.getNumeroPedido());
        System.out.println("Alterações no pedido concluídas.");
    }

    private void excluirPedido() {
        System.out.println("\n--- EXCLUIR PEDIDO DE VENDA ---");
        List<PedidoVenda> pedidosAtivos = pedidos.stream().filter(PedidoVenda::isAtivo).collect(Collectors.toList());
        if (pedidosAtivos.isEmpty()) {
            System.out.println("Nenhum pedido ativo para excluir.");
            return;
        }
        System.out.println("\nSelecione o pedido a ser excluído:");
        for (PedidoVenda pedido : pedidosAtivos) {
            Pessoa cliente = buscarPessoaPorId(pedido.getIdCliente());
            String nomeCliente = (cliente != null) ? cliente.getNome() : "Cliente não encontrado";
            System.out.printf("Nº: %d | Cliente: %s | Total: R$ %.2f\n", pedido.getNumeroPedido(), nomeCliente,
                    pedido.getTotalPedido());
        }
        int numeroPedido = menu.lerEntradaIntVerificado("\nDigite o número do pedido a excluir (desativar): ");
        PedidoVenda pedido = buscarPedidoPorNumero(numeroPedido);
        if (pedido != null && pedido.isAtivo()) {
            pedido.setAtivo(false);
            gerarLog("Pedido de Venda desativado: Nº " + pedido.getNumeroPedido());
            System.out.println("Pedido desativado com sucesso.");
        } else {
            System.out.println("Pedido não encontrado ou já está inativo.");
        }
    }

    private Pessoa selecionarClienteParaPedido() {
        System.out.println("\nSelecione o Cliente:");
        List<Pessoa> clientesAtivos = pessoas.stream().filter(p -> p instanceof Cliente && p.isAtivo())
                .collect(Collectors.toList());
        if (clientesAtivos.isEmpty()) {
            System.out.println("Nenhum cliente ativo encontrado. Cadastre um cliente primeiro.");
            return null;
        }
        for (int i = 0; i < clientesAtivos.size(); i++) {
            Pessoa cliente = clientesAtivos.get(i);
            System.out.printf("%d - %s (ID: %d)\n", (i + 1), cliente.getNome(), cliente.getId());
        }
        int escolha = menu.lerEntradaIntVerificado("Escolha o número do cliente: ");
        if (escolha > 0 && escolha <= clientesAtivos.size()) {
            return clientesAtivos.get(escolha - 1);
        } else {
            System.out.println("Seleção inválida.");
            return null;
        }
    }

    private TipoVenda selecionarTipoVenda() {
        while (true) {
            int escolha = menu.lerEntradaIntVerificado("\nSelecione o tipo de venda (1 - Presencial, 2 - Entrega): ");
            if (escolha == 1)
                return TipoVenda.PRESENCIAL;
            if (escolha == 2)
                return TipoVenda.ENTREGA;
            System.out.println("Opção inválida.");
        }
    }

    private Endereco selecionarEnderecoParaPedido(Pessoa cliente) {
        System.out.println("\nSelecione o Endereço de Entrega:");
        List<Endereco> enderecos = cliente.getEnderecos();
        if (enderecos.isEmpty()) {
            System.out.println("ERRO: Este cliente não possui endereços cadastrados para entrega.");
            return null;
        }
        for (int i = 0; i < enderecos.size(); i++) {
            System.out.println((i + 1) + " - " + enderecos.get(i));
        }
        int escolha = menu.lerEntradaIntVerificado("Escolha o número do endereço: ");
        if (escolha > 0 && escolha <= enderecos.size()) {
            return enderecos.get(escolha - 1);
        } else {
            System.out.println("Seleção inválida.");
            return null;
        }
    }

    private void adicionarItensAoPedido(PedidoVenda pedido) {
        while (true) {
            menu.limparTela();
            System.out.println("\n--- Adicionar Item ao Pedido ---");
            Produto produto = selecionarProdutoParaPedido();
            if (produto == null) {
                System.out.println("Nenhum produto selecionado.");
                break;
            }
            int quantidade = menu.lerEntradaIntVerificado("Digite a quantidade: ");
            if (quantidade <= 0) {
                System.out.println("Quantidade deve ser maior que zero.");
                continue;
            }
            ItemVenda item = new ItemVenda(produto.getCodigo(), quantidade, produto.getPrecoVenda());
            pedido.adicionarItem(item);
            System.out.printf("Item adicionado: %d x %s\n", quantidade, produto.getDescricao());
            String continuar = menu.lerEntradaString("Adicionar mais um item? (s/n): ");
            if (!continuar.equalsIgnoreCase("s")) {
                break;
            }
        }
    }

    private Produto selecionarProdutoParaPedido() {
        System.out.println("\nSelecione o Produto:");
        List<Produto> produtosAtivos = produtos.stream().filter(Produto::isAtivo).collect(Collectors.toList());
        if (produtosAtivos.isEmpty()) {
            System.out.println("Nenhum produto ativo encontrado.");
            return null;
        }
        for (int i = 0; i < produtosAtivos.size(); i++) {
            Produto p = produtosAtivos.get(i);
            System.out.printf("%d - %s (Preço: R$ %.2f)\n", (i + 1), p.getDescricao(), p.getPrecoVenda());
        }
        int escolha = menu.lerEntradaIntVerificado("Escolha o número do produto: ");
        if (escolha > 0 && escolha <= produtosAtivos.size()) {
            return produtosAtivos.get(escolha - 1);
        } else {
            System.out.println("Seleção inválida.");
            return null;
        }
    }

    private void removerItemDePedido(PedidoVenda pedido) {
        if (pedido.getItens().isEmpty()) {
            System.out.println("Este pedido não possui itens para remover.");
            return;
        }
        System.out.println("\n--- Remover Item do Pedido Nº " + pedido.getNumeroPedido() + " ---");
        List<ItemVenda> itens = pedido.getItens();
        for (int i = 0; i < itens.size(); i++) {
            Produto produto = buscarProdutoPorCodigo(itens.get(i).getIdProduto());
            String descProduto = (produto != null) ? produto.getDescricao() : "Produto não encontrado";
            System.out.printf("%d - %s (Qtd: %d)\n", (i + 1), descProduto, itens.get(i).getQuantidade());
        }
        int escolha = menu.lerEntradaIntVerificado("Digite o número do item a remover (ou 0 para cancelar): ");
        if (escolha == 0) {
            return;
        }
        int indiceParaRemover = escolha - 1;
        if (indiceParaRemover >= 0 && indiceParaRemover < itens.size()) {
            ItemVenda itemRemovido = itens.remove(indiceParaRemover);
            Produto produtoRemovido = buscarProdutoPorCodigo(itemRemovido.getIdProduto());
            String descProdutoRemovido = (produtoRemovido != null) ? produtoRemovido.getDescricao()
                    : "ID " + itemRemovido.getIdProduto();
            gerarLog("Item removido do Pedido Nº " + pedido.getNumeroPedido() + ": " + descProdutoRemovido);
            System.out.println("Item removido com sucesso.");
        } else {
            System.out.println("Número de item inválido.");
        }
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

    private Produto buscarProdutoPorCodigo(int codigo) {
        for (Produto p : produtos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }

    private PedidoVenda buscarPedidoPorNumero(int numero) {
        for (PedidoVenda pedido : pedidos) {
            if (pedido.getNumeroPedido() == numero) {
                return pedido;
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