package com.ghost.restaurante_springboot.services;

import com.ghost.restaurante_springboot.models.ItemPedido;
import com.ghost.restaurante_springboot.models.ItemPedidoId;
import com.ghost.restaurante_springboot.models.Pedido;
import com.ghost.restaurante_springboot.models.Prato;
import com.ghost.restaurante_springboot.repositories.ClienteRepository;
import com.ghost.restaurante_springboot.repositories.ItemPedidoRepository;
import com.ghost.restaurante_springboot.repositories.PedidoRepository;
import com.ghost.restaurante_springboot.repositories.PratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PratoRepository pratoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    // ==========================================================
    // === MÉTODOS DE OPERAÇÃO DE PEDIDOS (CRUD + ITENS)
    // ==========================================================

    @Transactional
    public Pedido iniciarNovoPedido(Pedido pedido) {
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus(true); // aberto
        if (pedido.getValorTotal() == null) {
            pedido.setValorTotal(BigDecimal.ZERO);
        }
        return pedidoRepository.save(pedido);
    }

    public Pedido buscarPedidoPorId(Integer id) {
        Optional<Pedido> op = pedidoRepository.findById(id);
        return op.orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + id));
    }

    @Transactional
    public void adicionarItem(Integer pedidoId, Integer pratoId, Integer quantidade, String observacao) {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        Prato prato = pratoRepository.findById(pratoId)
                .orElseThrow(() -> new RuntimeException("Prato não encontrado: " + pratoId));

        ItemPedidoId id = new ItemPedidoId();
        id.setPedidoId(pedido.getId());
        id.setPratoId(prato.getId());

        Optional<ItemPedido> opItem = itemPedidoRepository.findById(id);
        ItemPedido item;

        if (opItem.isPresent()) {
            item = opItem.get();
            item.setQuantidade(item.getQuantidade() + quantidade);
            if (observacao != null) item.setObservacao(observacao);
        } else {
            item = new ItemPedido();
            item.setId(id);
            item.setPedido(pedido);
            item.setPrato(prato);
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(prato.getPrecoVenda());
            item.setObservacao(observacao);
        }

        itemPedidoRepository.save(item);

        pedido.setValorTotal(calcularTotalDoPedido(pedido));
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void finalizarPedido(Integer id) {
        Pedido pedido = buscarPedidoPorId(id);
        pedido.setStatus(false); // fechado
        pedido.setValorTotal(calcularTotalDoPedido(pedido));
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void cancelarPedido(Integer id) {
        Pedido pedido = buscarPedidoPorId(id);

        if (pedido.getItens() != null) {
            for (ItemPedido item : List.copyOf(pedido.getItens())) {
                itemPedidoRepository.delete(item);
            }
        }

        pedido.setStatus(false);
        pedido.setValorTotal(BigDecimal.ZERO);
        pedidoRepository.save(pedido);
    }

    private BigDecimal calcularTotalDoPedido(Pedido pedido) {
        Pedido p = pedidoRepository.findById(pedido.getId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado: " + pedido.getId()));

        if (p.getItens() == null || p.getItens().isEmpty()) {
            return BigDecimal.ZERO;
        }

        return p.getItens().stream()
                .map(it -> it.getPrecoUnitario().multiply(BigDecimal.valueOf(it.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ==========================================================
    // === MÉTODOS PARA DASHBOARD
    // ==========================================================

    /**
     * Calcula o total de vendas (somando valorTotal) dos pedidos fechados de hoje.
     */
    public BigDecimal calcularTotalVendasHoje() {
        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();
        List<Pedido> pedidos = pedidoRepository.findByDataPedidoAfterAndStatus(inicioDoDia, false);

        return pedidos.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Conta o número de pedidos fechados (status = false) de hoje.
     */
    public Long contarVendasHoje() {
        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();
        return pedidoRepository.countByDataPedidoAfterAndStatus(inicioDoDia, false);
    }

    /**
     * Busca todos os pedidos com status = true (abertos).
     */
    public List<Pedido> buscarPedidosAbertos() {
        return pedidoRepository.findByStatus(true);
    }

    /**
     * Busca os 10 pedidos mais recentes fechados (status = false).
     */
    public List<Pedido> buscarPedidosFinalizados() {
        return pedidoRepository.findTop10ByStatusOrderByDataPedidoDesc(false);
    }

    /**
     * Retorna o top 5 de pratos mais vendidos hoje.
     * Usa agrupamento de ItemPedido.
     */
    public List<Object[]> buscarTopProdutosVendidosHoje() {
        LocalDateTime inicioDoDia = LocalDate.now().atStartOfDay();

        // Consulta nativa simples: top 5 produtos mais vendidos no dia
        // Dependendo do banco, você pode criar uma query no repository,
        // mas aqui faremos uma JPQL simples com EntityManager se necessário.
        //
        // Por simplicidade, vamos retornar vazio se não tiver query nativa definida.
        // (pode adaptar depois para JPQL real)
        return List.of(); // Placeholder até criar query específica no repositório
    }
}
