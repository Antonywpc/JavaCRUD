package modelo;

import java.util.ArrayList;
import java.util.List;
import modelo.enums.TipoVenda;

public class PedidoVenda {
    private static int contador = 1;
    private int numeroPedido;
    private int idCliente;
    private TipoVenda tipoVenda;
    private Endereco enderecoEntrega; // Pode ser null se a venda for presencial
    private List<ItemVenda> itens;
    private double totalPedido;
    private boolean ativo;

    public PedidoVenda(int idCliente, TipoVenda tipoVenda, Endereco enderecoEntrega) {
        this.numeroPedido = contador++;
        this.idCliente = idCliente;
        this.tipoVenda = tipoVenda;
        this.enderecoEntrega = enderecoEntrega; // Armazena o objeto Endereco
        this.itens = new ArrayList<>();
        this.totalPedido = 0;
        this.ativo = true;
    }

    // --- Getters ---
    public int getNumeroPedido() { return numeroPedido; }
    public int getIdCliente() { return idCliente; }
    public TipoVenda getTipoVenda() { return tipoVenda; }
    public Endereco getEnderecoEntrega() { return enderecoEntrega; }
    public List<ItemVenda> getItens() { return itens; }
    public double getTotalPedido() { return totalPedido; }
    public boolean isAtivo() { return ativo; }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    // --- Métodos de Lógica ---

    public void adicionarItem(ItemVenda item) {
        this.itens.add(item);
        recalcularTotalPedido(); // Recalcula o total sempre que um item é adicionado
    }

    public void recalcularTotalPedido() {
        this.totalPedido = 0;
        for (ItemVenda item : this.itens) {
            this.totalPedido += item.getValorTotalItem();
        }
    }
}