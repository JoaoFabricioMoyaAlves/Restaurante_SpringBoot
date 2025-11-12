package com.ghost.restaurante_springboot.models;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "ingredientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Coluna 'descricao' no banco
    @Column(name = "descricao", nullable = false, unique = true)
    private String descricao;

    // Preço unitário (coluna valor_unt)
    @Column(name = "valor_unt", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnt = BigDecimal.ZERO;

    // Unidade de medida (FK)
    @ManyToOne
    @JoinColumn(name = "unidade_medida_id", nullable = false)
    private UnidadeMedida unidadeMedida;

    // Estoque em banco: qt_estoque
    @Column(name = "qt_estoque", nullable = false, precision = 10, scale = 3)
    private BigDecimal qtEstoque = BigDecimal.ZERO;
}
