package com.ghost.restaurante_springboot.models;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable
@Data // Usa Lombok para getters/setters/hashCode/equals
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoId implements Serializable {

    private Integer pedidoId;
    private Integer pratoId;

    // O Lombok cuida dos métodos hashCode e equals, que são CRUCIAIS
    // para chaves compostas.
}