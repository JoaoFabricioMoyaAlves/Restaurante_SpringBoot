package com.ghost.restaurante_springboot.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "unidade_medida")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnidadeMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Ex: "Quilograma", "Litro", "Unidade"
    @Column(nullable = false, unique = true)
    private String nome; 

    // Ex: "kg", "L", "un"
    @Column(nullable = false, unique = true, length = 5)
    private String sigla; 
}