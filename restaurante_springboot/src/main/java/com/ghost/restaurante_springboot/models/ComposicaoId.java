package com.ghost.restaurante_springboot.models;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.Data; 
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Embeddable 
@Data // Inclui Getters, Setters, hashCode e equals
@NoArgsConstructor
@AllArgsConstructor
public class ComposicaoId implements Serializable {

    @Column(name = "prato_id")
    private Integer pratoId;

    @Column(name = "ingrediente_id")
    private Integer ingredienteId;
    
    // O Lombok gera equals/hashCode automaticamente, mas se não o estiver a usar,
    // o código manual deve ser incluído aqui, como fizemos em ItemPedidoId.
}