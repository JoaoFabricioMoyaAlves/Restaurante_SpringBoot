package com.ghost.restaurante_springboot.services;

import com.ghost.restaurante_springboot.models.UnidadeMedida;
import com.ghost.restaurante_springboot.repositories.UnidadeMedidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnidadeMedidaService {

    @Autowired
    private UnidadeMedidaRepository unidadeMedidaRepository;

    public List<UnidadeMedida> listarTodas() {
        return unidadeMedidaRepository.findAll();
    }

    public UnidadeMedida salvar(UnidadeMedida unidade) {
        return unidadeMedidaRepository.save(unidade);
    }

    public UnidadeMedida buscarPorId(Integer id) {
        return unidadeMedidaRepository.findById(id).orElse(null);
    }

    public void excluir(Integer id) {
        unidadeMedidaRepository.deleteById(id);
    }
}
