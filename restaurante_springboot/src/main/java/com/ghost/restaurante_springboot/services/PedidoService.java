package com.ghost.restaurante_springboot.services;

import com.ghost.restaurante_springboot.models.ItemPedido;
import com.ghost.restaurante_springboot.models.Pedido;
import com.ghost.restaurante_springboot.models.Prato;
import com.ghost.restaurante_springboot.repositories.ItemPedidoRepository; 
import com.ghost.restaurante_springboot.repositories.PedidoRepository;
import com.ghost.restaurante_springboot.repositories.PratoRepository; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException; 

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired 
    private PratoRepository pratoRepository; 
    
    @Autowired 
    private ItemPedidoRepository itemPedidoRepository; 

    // ===============================================
    // MÉTODOS EXISTENTES E NOVOS MÉTODOS DE CÁLCULO
    // ===============================================

    @Transactional
    public Pedido iniciarNovoPedido(Pedido pedido) {
        // Assegura que o novo pedido tenha os valores iniciais corretos
        pedido.setDataPedido(LocalDateTime.now()); 
        pedido.setStatus(true); // Status: ABERTO
        pedido.setValorTotal(BigDecimal.ZERO); // Valor inicial zerado
        
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> buscarPedidosFechadosDeHoje() {
        // Pega o início do dia
        LocalDateTime hoje = LocalDate.now().atStartOfDay();
        
        // Busca pedidos após o início do dia e com status FECHADO (false)
        return pedidoRepository.findByDataPedidoAfterAndStatus(hoje, false);
    }

    public BigDecimal calcularTotalVendasHoje() {
        // Lógica simples: busca todos os pedidos finalizados hoje e soma o valor
        List<Pedido> vendasFechadas = buscarPedidosFechadosDeHoje();
        
        return vendasFechadas.stream()
                .map(Pedido::getValorTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * NOVO MÉTODO NECESSÁRIO para o dashboard (vendasHojeCount).
     * Retorna a contagem de pedidos com status FECHADO (FALSE) feitos hoje.
     */
    public Long contarVendasHoje() {
        LocalDateTime hoje = LocalDate.now().atStartOfDay();
        
        // Assumindo que o Repositório possui um método como countByDataPedidoAfterAndStatus
        // Se não tiver, o método abaixo funcionará, mas será menos eficiente.
        return pedidoRepository.countByDataPedidoAfterAndStatus(hoje, false);
    }
    
    /**
     * NOVO MÉTODO NECESSÁRIO para o dashboard (pedidosAbertos).
     * Retorna todos os pedidos com status ABERTO (TRUE).
     */
    public List<Pedido> buscarPedidosAbertos() {
        return pedidoRepository.findByStatus(true);
    }
    
    /**
     * Busca um Pedido por ID, lançando 404 se não for encontrado.
     */
    public Pedido buscarPedidoPorId(Integer id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado com ID: " + id));
    }

    /**
     * Adiciona um prato a um pedido existente e recalcula o valor total.
     */
    @Transactional
    public void adicionarItem(Integer pedidoId, Integer pratoId, Integer quantidade, String observacao) {
        
        // 1. Buscar Pedido e Prato
        Pedido pedido = buscarPedidoPorId(pedidoId);
        Prato prato = pratoRepository.findById(pratoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prato não encontrado com ID: " + pratoId));

        // Validação
        if (!pedido.isStatus()) {
            throw new IllegalStateException("Não é possível adicionar itens a um pedido que não está aberto.");
        }
        if (quantidade == null || quantidade <= 0) {
            throw new IllegalArgumentException("A quantidade deve ser maior que zero.");
        }
        
        // 2. Criar Novo Item
        ItemPedido novoItem = new ItemPedido();
        
        // Configura as chaves e associa as entidades
        novoItem.setPedido(pedido); 
        novoItem.setPrato(prato);    

        novoItem.setQuantidade(quantidade);
        novoItem.setPrecoUnitario(prato.getPrecoVenda()); 
        novoItem.setObservacao(observacao);

        // 3. Persistir o ItemPedido (importante para evitar problemas de persistência)
        itemPedidoRepository.save(novoItem);
        
        // 4. Adicionar o item à coleção do Pedido (se for bidirecional)
        if (pedido.getItens() != null) {
            pedido.getItens().add(novoItem);
        }
        
        // 5. Recalcular o Valor Total do Pedido
        BigDecimal novoTotal = pedido.getItens().stream()
                .map(ItemPedido::calcularSubtotal) 
                .reduce(BigDecimal.ZERO, BigDecimal::add);
                
        pedido.setValorTotal(novoTotal);
        
        // 6. Salvar o pedido (persiste o novo total)
        pedidoRepository.save(pedido); 
    }
    
    // ===============================================
    // OUTROS MÉTODOS DO DASHBOARD (MOCK/EXEMPLO)
    // ===============================================

    /**
     * MOCK: Retorna pedidos finalizados recentemente para o dashboard.
     */
    public List<Pedido> buscarPedidosFinalizados() {
        // Busca os últimos 10 pedidos com status FALSE (fechado)
        return pedidoRepository.findTop10ByStatusOrderByDataPedidoDesc(false);
    }
    
    /**
     * MOCK: Retorna as transações recentes (pode ser o mesmo que Pedidos Finalizados).
     */
    public List<Pedido> findTransacoesRecentes() {
        // Por simplificação, retorna os últimos 5 pedidos, abertos ou fechados
        return pedidoRepository.findTop5ByOrderByDataPedidoDesc(); 
    }
}