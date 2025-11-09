package com.ghost.restaurante_springboot.repositories;

import com.ghost.restaurante_springboot.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
    // Método customizado que pode ser usado no Dashboard (Ex: Novos Clientes)
    // Busca os clientes mais recentes pelo ID (assumindo que IDs maiores são mais recentes)
    List<Cliente> findTop5ByOrderByIdDesc();
    
}