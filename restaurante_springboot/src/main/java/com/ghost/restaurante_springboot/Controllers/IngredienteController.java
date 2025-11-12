package com.ghost.restaurante_springboot.Controllers;

import com.ghost.restaurante_springboot.models.Ingrediente;
import com.ghost.restaurante_springboot.models.UnidadeMedida;
import com.ghost.restaurante_springboot.services.IngredienteService;
import com.ghost.restaurante_springboot.services.UnidadeMedidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/ingredientes")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @Autowired
    private UnidadeMedidaService unidadeMedidaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("ingredientes", ingredienteService.listarTodos());
        return "ingredientes/lista";
    }

    @GetMapping("/novo")
    public String novoIngrediente(Model model) {
        Ingrediente ingrediente = new Ingrediente();
        List<UnidadeMedida> unidades = unidadeMedidaService.listarTodas();

        model.addAttribute("ingrediente", ingrediente);
        model.addAttribute("unidadesMedida", unidades);

        return "ingredientes/form";
    }

    @PostMapping("/salvar")
    public String salvarIngrediente(@ModelAttribute Ingrediente ingrediente) {
        ingredienteService.salvar(ingrediente);
        return "redirect:/ingredientes";
    }

    @GetMapping("/excluir/{id}")
  public String excluir(@PathVariable Integer id) {
    ingredienteService.excluir(id);
    return "redirect:/ingredientes";
}

}
