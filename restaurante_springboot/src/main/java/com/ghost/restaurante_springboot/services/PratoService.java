
package com.ghost.restaurante_springboot.services;

import com.ghost.restaurante_springboot.models.Prato;
import com.ghost.restaurante_springboot.repositories.PratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PratoService {

    @Autowired
    private PratoRepository pratoRepository;

    public List<Prato> findAll() {
        return pratoRepository.findAll();
    }

    public Optional<Prato> findById(Integer id) {
        return pratoRepository.findById(id);
    }

    public Prato save(Prato prato) {
        return pratoRepository.save(prato);
    }

    public void deleteById(Integer id) {
        pratoRepository.deleteById(id);
    }

    public List<Prato> findAtivos() {
        return pratoRepository.findByAtivoTrue();
    }
}
