package com.ghost.restaurante_springboot.repositories;

import com.ghost.restaurante_springboot.models.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Integer> {

    // Busca ingredientes com estoque menor que um valor
    List<Ingrediente> findByQuantidadeEstoqueLessThan(BigDecimal valor);

    // Exemplo: busca ingrediente pelo nome (opcional, pode remover se não usa)
    Ingrediente findByNome(String nome);
}
