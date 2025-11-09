package com.ghost.restaurante_springboot.Controllers;

import com.ghost.restaurante_springboot.services.PedidoService;
import com.ghost.restaurante_springboot.models.Pedido; // Adicionado para lidar com Pedidos Abertos/Finalizados, se necessário
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

    @GetMapping({"/", "/dashboard"}) // Mapeia a raiz e /dashboard para o mesmo método
    public String viewDashboard(Model model) {
        
        // =======================================================
        // 1. CÁLCULO DE VENDAS HOJE (CORRIGIDO)
        // =======================================================
        
        // 1.1. Calcula o total de vendas (Vendas Hoje)
        BigDecimal vendasHojeTotal = pedidoService.calcularTotalVendasHoje();
        
        // 1.2. Conta o número de transações (Vendas Hoje)
        // Assumindo que você tem um método para contar as vendas
        Long vendasHojeCount = pedidoService.contarVendasHoje(); 
        
        // CORREÇÃO ESSENCIAL: Adicionando as chaves que o Thymeleaf espera
        model.addAttribute("vendasHojeTotal", vendasHojeTotal);
        model.addAttribute("vendasHojeCount", vendasHojeCount); 
        
        
        // =======================================================
        // 2. OUTROS DADOS DO DASHBOARD (EXEMPLOS COM BASE NO HTML)
        // Você deve criar métodos correspondentes no PedidoService
        // =======================================================
        
        // Pedidos Abertos (para o painel desktop-8)
        // model.addAttribute("pedidosAbertos", pedidoService.findPedidosAbertos()); 
        
        // Pedidos Finalizados (para o painel desktop-7)
        // model.addAttribute("pedidosFinalizados", pedidoService.findPedidosFinalizados());

        // Estoque Baixo (para o painel desktop-2)
        // model.addAttribute("estoqueBaixo", estoqueService.findItensEstoqueBaixo());
        
        // Novos Clientes (para o painel desktop-5)
        // model.addAttribute("novosClientes", clienteService.findNovosClientes());

        // Transações Recentes (para o painel desktop-6)
        // model.addAttribute("transacoesRecentes", pedidoService.findTransacoesRecentes());

        // Top Produtos (para o painel desktop-9)
        // model.addAttribute("topProdutos", produtoService.findTopProdutos(5));


        // =======================================================
        // 3. RETORNO
        // =======================================================
        return "dashboard"; // Retorna para a sua view dashboard.html
    }
}