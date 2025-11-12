package com.ghost.restaurante_springboot.repositories;

import com.ghost.restaurante_springboot.models.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {
    Ingrediente findByDescricao(String descricao);
}
