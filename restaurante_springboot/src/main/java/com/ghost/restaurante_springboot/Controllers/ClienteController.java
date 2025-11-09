package com.ghost.restaurante_springboot.Controllers;

import com.ghost.restaurante_springboot.models.Cliente;
import com.ghost.restaurante_springboot.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    // --- 1. LISTAGEM (READ - Template: clientes/lista.html) ---
    // ✅ CORRIGIDO: Mapeado para a URL base /clientes
    @GetMapping 
    public String listarClientes(Model model) {
        List<Cliente> clientes = clienteService.listarTodos();
        model.addAttribute("clientes", clientes);
        // O template continua sendo "clientes/lista"
        return "clientes/lista"; 
    }

    // --- 2. NOVO/EDIÇÃO (CREATE/UPDATE - Template: clientes/cadastro.html) ---
    // Ajustado para usar @PathVariable para edição, o que é mais limpo.
    @GetMapping({"/novo", "/editar/{id}"}) 
    public String exibirFormulario(@PathVariable(required = false) Integer id, Model model) {
        Cliente cliente;
        
        if (id != null) {
            // Se um ID for fornecido, busca o cliente para edição
            cliente = clienteService.buscarPorId(id);
            if (cliente == null) {
                // Se o cliente não for encontrado (404), tratamos elegantemente
                cliente = new Cliente();
                // Opcional: Adicionar mensagem de erro para o usuário aqui.
            }
        } else {
            // Se nenhum ID for fornecido, cria um novo cliente
            cliente = new Cliente();
        }
        
        model.addAttribute("cliente", cliente); 
        return "clientes/cadastro";
    }

    // --- 3. SALVAR (CREATE/UPDATE) ---
    @PostMapping("/salvar")
    public String salvarCliente(@ModelAttribute Cliente cliente, RedirectAttributes ra) {
        
        String acao = (cliente.getId() == null) ? "criado" : "atualizado";
        
        clienteService.salvar(cliente);
        ra.addFlashAttribute("mensagemSucesso", "Cliente **" + cliente.getNome() + "** " + acao + " com sucesso! 🎉");
        
        // ✅ Redireciona para a URL base /clientes
        return "redirect:/clientes";
    }
    
    // --- 4. EXCLUSÃO (DELETE) ---
    @GetMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Integer id, RedirectAttributes ra) {
        
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente != null) {
            clienteService.excluir(id);
            ra.addFlashAttribute("mensagemAlerta", "Cliente **" + cliente.getNome() + "** excluído com sucesso. 🗑️");
        } else {
            ra.addFlashAttribute("mensagemErro", "Cliente não encontrado. ⚠️");
        }
        
        // ✅ Redireciona para a URL base /clientes
        return "redirect:/clientes";
    }
}