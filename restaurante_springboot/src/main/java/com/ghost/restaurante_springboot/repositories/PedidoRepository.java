package com.ghost.restaurante_springboot.repositories;

import com.ghost.restaurante_springboot.models.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    
    /**
     * Busca a lista de pedidos finalizados (status = false) após uma determinada data/hora.
     * Usado por PedidoService.buscarPedidosFechadosDeHoje()
     */
    List<Pedido> findByDataPedidoAfterAndStatus(LocalDateTime data, boolean status);

    // ===============================================
    // MÉTODOS ADICIONADOS PARA O DASHBOARD
    // ===============================================
    
    /**
     * Conta o número de pedidos finalizados (status = false) após uma determinada data/hora.
     * Usado por PedidoService.contarVendasHoje()
     */
    Long countByDataPedidoAfterAndStatus(LocalDateTime data, boolean status);
    
    /**
     * Busca todos os pedidos com base no status (true para abertos, false para fechados).
     * Usado por PedidoService.buscarPedidosAbertos()
     */
    List<Pedido> findByStatus(boolean status);

    /**
     * Busca os N pedidos mais recentes com base no status, ordenados pela data do pedido.
     * Usado por PedidoService.buscarPedidosFinalizados()
     * (O Spring infere o 'Top10' se configurarmos o Service para tal, mas o Query Method permite Top/First N).
     */
    List<Pedido> findTop10ByStatusOrderByDataPedidoDesc(boolean status);

    /**
     * Busca os 5 pedidos mais recentes, independentemente do status.
     * Usado por PedidoService.findTransacoesRecentes()
     */
    List<Pedido> findTop5ByOrderByDataPedidoDesc();
}