package com.ghost.restaurante_springboot.repositories;

import com.ghost.restaurante_springboot.models.Prato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PratoRepository extends JpaRepository<Prato, Integer> {
    
    // Busca todos os Pratos onde o campo 'ativo' é TRUE.
    List<Prato> findByAtivoTrue();
}