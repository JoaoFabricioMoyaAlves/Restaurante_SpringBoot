package com.ghost.restaurante_springboot.Controllers;

import com.ghost.restaurante_springboot.models.Pedido;
import com.ghost.restaurante_springboot.models.Prato; 
import com.ghost.restaurante_springboot.repositories.ClienteRepository;
import com.ghost.restaurante_springboot.repositories.PedidoRepository; // NOVO IMPORT NECESSÁRIO
import com.ghost.restaurante_springboot.repositories.PratoRepository; 
import com.ghost.restaurante_springboot.services.PedidoService; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    // Injetamos o Serviço para a lógica de negócio
    @Autowired 
    private PedidoService pedidoService;
    
    // Injetamos os Repositórios de suporte necessários para carregar dados nas views
    @Autowired
    private ClienteRepository clienteRepository; 
    
    @Autowired
    private PratoRepository pratoRepository; 
    
    // ✅ NOVO: Injetamos o PedidoRepository para o método listarPedidos
    @Autowired 
    private PedidoRepository pedidoRepository; 

    // ===============================================
    // ✅ NOVO MÉTODO: LISTAR PEDIDOS (Resolve o 404 em /pedidos)
    // ===============================================
    /**
     * Mapeia a URL base /pedidos e retorna a lista de todos os pedidos.
     */
    @GetMapping 
    public ModelAndView listarPedidos() {
        // Cria a ModelAndView, apontando para sua template de lista
        ModelAndView mv = new ModelAndView("pedidos/lista"); 
        
        // Busca todos os pedidos e adiciona ao Model
        List<Pedido> pedidos = pedidoRepository.findAll(); 
        mv.addObject("pedidos", pedidos); 
        
        return mv;
    }
    
    // Método para exibir o formulário de novo pedido: OK
    @GetMapping("/novo")
    public ModelAndView novoPedido() {
        ModelAndView mv = new ModelAndView("pedidos/novo");
        mv.addObject("pedido", new Pedido());
        mv.addObject("clientes", clienteRepository.findAll());
        return mv;
    }

    /**
     * Salva o novo pedido usando o Service e redireciona para a edição/adição de itens.
     */
    @PostMapping("/iniciar")
    public String iniciarPedido(Pedido pedido) {
        // Lógica de iniciação delegada ao Service
        Pedido novoPedido = pedidoService.iniciarNovoPedido(pedido);
        
        // Redireciona para a página de adicionar itens.
        return "redirect:/pedidos/editar/" + novoPedido.getId();
    }
    
    /**
     * Tela de edição e adição de itens (Desktop - 4)
     * Carrega o pedido e a lista de pratos disponíveis.
     */
    @GetMapping("/editar/{id}")
    public ModelAndView editarPedido(@PathVariable Integer id) {
        ModelAndView mv = new ModelAndView("pedidos/editar");
        
        // 1. Busca o pedido usando o Service (incluindo tratamento de erro)
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        mv.addObject("pedido", pedido);
        
        // 2. Carrega a lista de pratos ATIVOS para o operador adicionar ao pedido
        List<Prato> pratosDisponiveis = pratoRepository.findByAtivoTrue(); 
        mv.addObject("pratos", pratosDisponiveis);

        return mv;
    }

    // ===============================================
    // MÉTODO: ADICIONAR ITEM AO PEDIDO
    // ===============================================
    @PostMapping("/adicionar-item/{pedidoId}")
    public String adicionarItem(
        @PathVariable Integer pedidoId,
        @RequestParam Integer pratoId, // ID do prato selecionado no formulário
        @RequestParam(defaultValue = "1") Integer quantidade, // Quantidade, padrão 1
        @RequestParam(required = false) String observacao // Observação opcional
    ) {
        // 1. Chama o Service para executar a lógica de adicionar o item
        pedidoService.adicionarItem(pedidoId, pratoId, quantidade, observacao);
        
        // 2. Redireciona de volta para a tela de edição para continuar o pedido
        return "redirect:/pedidos/editar/" + pedidoId;
    }

    
}