package com.ghost.restaurante_springboot.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "pratos")
public class Prato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, length = 60)
    private String nome;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    // Mapeado para a coluna 'preco_venda' (seu SQL)
    @Column(name = "preco_venda", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "tempo_preparo_min")
    private Integer tempoPreparoMin;

    // Mapeado para a coluna 'ativo' (seu SQL)
    @Column(name = "ativo", columnDefinition = "BOOLEAN")
    private boolean ativo = true;

    // Construtor padrão
    public Prato() {}

    // Getters e Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public Integer getTempoPreparoMin() {
        return tempoPreparoMin;
    }

    public void setTempoPreparoMin(Integer tempoPreparoMin) {
        this.tempoPreparoMin = tempoPreparoMin;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    // hashCode e equals
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prato prato = (Prato) o;
        return Objects.equals(id, prato.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}