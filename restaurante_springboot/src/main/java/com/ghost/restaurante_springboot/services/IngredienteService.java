package com.ghost.restaurante_springboot.services;

import com.ghost.restaurante_springboot.models.Ingrediente;
import com.ghost.restaurante_springboot.repositories.IngredienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    public List<Ingrediente> listarTodos() {
        return ingredienteRepository.findAll();
    }

    public Ingrediente salvar(Ingrediente ingrediente) {
        return ingredienteRepository.save(ingrediente);
    }

    public Ingrediente buscarPorId(Integer id) {
        return ingredienteRepository.findById(id).orElse(null);
    }

    public void excluir(Integer id) {
        ingredienteRepository.deleteById(id);
    }
    
}
