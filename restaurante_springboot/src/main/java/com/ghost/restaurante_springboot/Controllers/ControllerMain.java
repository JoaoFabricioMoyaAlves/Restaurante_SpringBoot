package com.ghost.restaurante_springboot.Controllers;

import com.ghost.restaurante_springboot.services.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class ControllerMain {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Exibe o dashboard com os dados de vendas, pedidos e produtos.
     */
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {

        // === DADOS DE VENDAS DO DIA ===
        BigDecimal vendasHoje = pedidoService.calcularTotalVendasHoje();
        Long qtdVendasHoje = pedidoService.contarVendasHoje();

        // === PEDIDOS ABERTOS E FINALIZADOS ===
        var pedidosAbertos = pedidoService.buscarPedidosAbertos();
        var pedidosFinalizados = pedidoService.buscarPedidosFinalizados();

        // === TOP PRODUTOS VENDIDOS ===
        List<Object[]> topProdutos = pedidoService.buscarTopProdutosVendidosHoje();

        // === ENVIA PARA O FRONT ===
        model.addAttribute("vendasHoje", vendasHoje);
        model.addAttribute("qtdVendasHoje", qtdVendasHoje);
        model.addAttribute("pedidosAbertos", pedidosAbertos);
        model.addAttribute("pedidosFinalizados", pedidosFinalizados);
        model.addAttribute("topProdutos", topProdutos);

        // === RETORNA A VIEW ===
        return "dashboard";
    }
}
