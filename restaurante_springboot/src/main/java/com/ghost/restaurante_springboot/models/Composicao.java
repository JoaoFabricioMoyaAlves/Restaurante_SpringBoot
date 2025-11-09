package com.ghost.restaurante_springboot.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data; 
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "composicao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Composicao {

    // 1. Chave embutida
    @EmbeddedId
    private ComposicaoId id;

    // 2. Mapeamento do Prato
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pratoId") // Mapeia o campo 'pratoId' da chave ComposicaoId
    @JoinColumn(name = "prato_id")
    private Prato prato;

    // 3. Mapeamento do Ingrediente
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredienteId") // Mapeia o campo 'ingredienteId' da chave ComposicaoId
    @JoinColumn(name = "ingrediente_id")
    private Ingrediente ingrediente; 

    // Quantidade NECESSÁRIA do ingrediente para FAZER o prato
    @Column(name = "quantidade_necessaria", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidadeNecessaria;
    
    // Métodos utilitários para configurar a relação
    public void setPrato(Prato prato) {
        this.prato = prato;
        if (this.id == null) {
            this.id = new ComposicaoId();
        }
        this.id.setPratoId(prato.getId());
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
        if (this.id == null) {
            this.id = new ComposicaoId();
        }
        this.id.setIngredienteId(ingrediente.getId());
    }
}