package com.ghost.restaurante_springboot.Controllers;

import com.ghost.restaurante_springboot.models.Pedido;
import com.ghost.restaurante_springboot.models.Prato;
import com.ghost.restaurante_springboot.repositories.ClienteRepository;
import com.ghost.restaurante_springboot.repositories.PedidoRepository;
import com.ghost.restaurante_springboot.repositories.PratoRepository;
import com.ghost.restaurante_springboot.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PratoRepository pratoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    // LISTAR PEDIDOS (GET /pedidos)
    @GetMapping
    public ModelAndView listarPedidos() {
        ModelAndView mv = new ModelAndView("pedidos/lista");
        List<Pedido> pedidos = pedidoRepository.findAll();
        mv.addObject("pedidos", pedidos);
        return mv;
    }

    // FORM PARA CRIAR NOVO PEDIDO (GET /pedidos/novo)
    @GetMapping("/novo")
    public ModelAndView novoPedido() {
        ModelAndView mv = new ModelAndView("pedidos/novo");
        mv.addObject("pedido", new Pedido());
        mv.addObject("clientes", clienteRepository.findAll());
        return mv;
    }

    // INICIAR PEDIDO (POST /pedidos/iniciar)
    @PostMapping("/iniciar")
    public String iniciarPedido(Pedido pedido) {
        Pedido novoPedido = pedidoService.iniciarNovoPedido(pedido);
        return "redirect:/pedidos/editar/" + novoPedido.getId();
    }

    // EDITAR PEDIDO (GET /pedidos/editar/{id})
    @GetMapping("/editar/{id}")
    public ModelAndView editarPedido(@PathVariable Integer id) {
        ModelAndView mv = new ModelAndView("pedidos/editar");

        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        mv.addObject("pedido", pedido);

        List<Prato> pratosDisponiveis = pratoRepository.findByAtivoTrue();
        mv.addObject("pratos", pratosDisponiveis);

        return mv;
    }

    // ADICIONAR ITEM AO PEDIDO (POST /pedidos/adicionar-item/{pedidoId})
    @PostMapping("/adicionar-item/{pedidoId}")
    public String adicionarItem(
            @PathVariable Integer pedidoId,
            @RequestParam Integer pratoId,
            @RequestParam(defaultValue = "1") Integer quantidade,
            @RequestParam(required = false) String observacao
    ) {
        pedidoService.adicionarItem(pedidoId, pratoId, quantidade, observacao);
        return "redirect:/pedidos/editar/" + pedidoId;
    }

    // FINALIZAR PEDIDO (POST /pedidos/finalizar/{id})
    @PostMapping("/finalizar/{id}")
    public String finalizarPedido(@PathVariable Integer id) {
        pedidoService.finalizarPedido(id);
        return "redirect:/pedidos";
    }

    // CANCELAR PEDIDO (POST /pedidos/cancelar/{id})
    @PostMapping("/cancelar/{id}")
    public String cancelarPedido(@PathVariable Integer id) {
        pedidoService.cancelarPedido(id);
        return "redirect:/pedidos";
    }
}
