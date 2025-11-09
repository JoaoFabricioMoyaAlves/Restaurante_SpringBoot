package com.ghost.restaurante_springboot.services;

import com.ghost.restaurante_springboot.models.Cliente;
import com.ghost.restaurante_springboot.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Salva ou atualiza um cliente no banco de dados.
     * @param cliente O objeto Cliente a ser salvo.
     * @return O objeto Cliente salvo/atualizado.
     */
    public Cliente salvar(Cliente cliente) {
        // Você pode adicionar validações aqui antes de salvar (ex: CPF válido)
        return clienteRepository.save(cliente);
    }
    
    /**
     * Lista todos os clientes.
     * @return Lista de todos os clientes.
     */
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
    
    /**
     * Busca os 5 clientes mais recentes para o Dashboard.
     * @return Lista dos 5 clientes mais recentes.
     */
    public List<Cliente> buscarTop5ClientesRecentes() {
        return clienteRepository.findTop5ByOrderByIdDesc();
    }
    
    /**
     * Busca um cliente pelo ID (usado para edição).
     * @param id O ID do cliente.
     * @return O cliente encontrado ou null.
     */
    public Cliente buscarPorId(Integer id) {
        return clienteRepository.findById(id).orElse(null);
    }
    // ClienteService.java

// ...

// Adicione este método ao seu ClienteService
public void excluir(Integer id) {
    clienteRepository.deleteById(id);
}
}