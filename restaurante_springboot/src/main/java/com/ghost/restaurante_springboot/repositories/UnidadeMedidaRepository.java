package com.ghost.restaurante_springboot.repositories;

import com.ghost.restaurante_springboot.models.UnidadeMedida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeMedidaRepository extends JpaRepository<UnidadeMedida, Integer> {
}
