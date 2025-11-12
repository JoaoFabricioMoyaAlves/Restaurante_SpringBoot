package com.ghost.restaurante_springboot.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ingredientes")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Usa a coluna real 'descricao'
    @Column(name = "descricao", nullable = false)
    private String descricao;

    // Usa a coluna real 'valor_unt'
    @Column(name = "valor_unt", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorUnt;

    // Usa a chave estrangeira real 'unidade_medida_id'
    @ManyToOne
    @JoinColumn(name = "unidade_medida_id", nullable = false)
    private UnidadeMedida unidadeMedida;

    // Usa a coluna real 'qt_estoque'
    @Column(name = "qt_estoque", nullable = false, precision = 10, scale = 3)
    private BigDecimal qtEstoque;

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValorUnt() {
        return valorUnt;
    }

    public void setValorUnt(BigDecimal valorUnt) {
        this.valorUnt = valorUnt;
    }

    public UnidadeMedida getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(UnidadeMedida unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getQtEstoque() {
        return qtEstoque;
    }

    public void setQtEstoque(BigDecimal qtEstoque) {
        this.qtEstoque = qtEstoque;
    }
}
