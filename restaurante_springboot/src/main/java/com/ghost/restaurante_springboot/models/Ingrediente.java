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

    @Column(nullable = false, unique = true)
    private String nome;

    // Relacionamento Muitos para Um: Muitos ingredientes usam uma Unidade de Medida
    @ManyToOne
    @JoinColumn(name = "unidade_medida_id", nullable = false)
    private UnidadeMedida unidadeMedida;

    // ESTOQUE: Quantidade atual em estoque
    // Deve ser um número decimal (BigDecimal) para precisão de pesos/litros
    @Column(name = "quantidade_estoque", nullable = false, precision = 10, scale = 3)
    private BigDecimal quantidadeEstoque = BigDecimal.ZERO; 
}