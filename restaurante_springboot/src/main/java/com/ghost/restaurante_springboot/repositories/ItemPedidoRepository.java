package com.ghost.restaurante_springboot.repositories;

import com.ghost.restaurante_springboot.models.ItemPedido;
import com.ghost.restaurante_springboot.models.ItemPedidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoId> {

    /**
     * Retorna os produtos mais vendidos no dia atual.
     * Agrupa por nome do prato e soma as quantidades.
     */
    @Query("SELECT i.prato.nome, SUM(i.quantidade) AS totalVendido " +
           "FROM ItemPedido i " +
           "WHERE i.pedido.dataPedido >= CURRENT_DATE " +
           "AND i.pedido.status = false " +
           "GROUP BY i.prato.nome " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findTopProdutosVendidosHoje();
}
