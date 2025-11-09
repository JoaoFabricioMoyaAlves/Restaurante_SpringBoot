package com.ghost.restaurante_springboot.repositories;

import com.ghost.restaurante_springboot.models.ItemPedido;
import com.ghost.restaurante_springboot.models.ItemPedidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoId> {
}