package com.ghost.restaurante_springboot.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ItemPedidoPK implements Serializable {

    // Não precisamos mapear a entidade aqui, apenas os IDs
    private Integer pedidoId;
    private Integer pratoId;

    // Construtores
    public ItemPedidoPK() {}

    public ItemPedidoPK(Integer pedidoId, Integer pratoId) {
        this.pedidoId = pedidoId;
        this.pratoId = pratoId;
    }

    // Getters e Setters
    public Integer getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Integer pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Integer getPratoId() {
        return pratoId;
    }

    public void setPratoId(Integer pratoId) {
        this.pratoId = pratoId;
    }

    // OBRIGATÓRIO: Implementar hashCode e equals para chaves compostas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedidoPK that = (ItemPedidoPK) o;
        return Objects.equals(pedidoId, that.pedidoId) && Objects.equals(pratoId, that.pratoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pedidoId, pratoId);
    }
}