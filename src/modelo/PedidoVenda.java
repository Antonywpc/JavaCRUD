package modelo;

import java.util.ArrayList;
import java.util.List;
import modelo.enums.TipoVenda;

public class PedidoVenda {
    private static int contador = 1;
    private int numeroPedido;
    private int idCliente;
    private TipoVenda tipoVenda;
    private Endereco enderecoEntrega;
    private List<ItemVenda> itens;
    private double totalPedido;
    private boolean ativo;

    public PedidoVenda(int idCliente, TipoVenda tipoVenda, Endereco enderecoEntrega) {
        this.numeroPedido = contador++;
        this.idCliente = idCliente;
        this.tipoVenda = tipoVenda;
        this.enderecoEntrega = enderecoEntrega; 
        this.itens = new ArrayList<>();
        // this.totalPedido = 0;
        this.ativo = true;
    }


    public int getNumeroPedido() { return numeroPedido; }
    public int getIdCliente() { return idCliente; }
    public TipoVenda getTipoVenda() { return tipoVenda; }
    public Endereco getEnderecoEntrega() { return enderecoEntrega; }
    public List<ItemVenda> getItens() { return itens; }

    public double getTotalPedido() { double total = 0.0;
    for (ItemVenda itemVenda : itens) {
        total += itemVenda.getPrecoUnitario() * itemVenda.getQuantidade();
    }
    return total; 
}

    public boolean isAtivo() { return ativo; }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    


    public void adicionarItem(PedidoVenda pedido, ItemVenda item, int quantidade) {
        for (ItemVenda itemVenda : pedido.getItens()) {
            if (itemVenda.getIdProduto() == item.getIdProduto()) {
                itemVenda.setQuantidade(itemVenda.getQuantidade() + quantidade);
                return;
            }
        }
        pedido.getItens().add(new ItemVenda(item.getIdProduto(), quantidade, item.getPrecoUnitario()));
    }

    public void recalcularTotalPedido() {
        this.totalPedido = 0;
        for (ItemVenda item : this.itens) {
            this.totalPedido += item.getValorTotalItem();
        }
    }
}