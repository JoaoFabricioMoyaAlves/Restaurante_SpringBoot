package com.ghost.restaurante_springboot.Controllers;

import com.ghost.restaurante_springboot.models.Prato;
import com.ghost.restaurante_springboot.services.PratoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/produtos")
public class PratoController {

    @Autowired
    private PratoService pratoService;

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("produtos", pratoService.findAll());
        return "produtos/lista";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("prato", new Prato());
        return "produtos/form";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute("prato") Prato prato, BindingResult result) {
        if (result.hasErrors()) {
            return "produtos/form";
        }
        pratoService.save(prato);
        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model) {
        Optional<Prato> op = pratoService.findById(id);
        if (op.isEmpty()) return "redirect:/produtos";
        model.addAttribute("prato", op.get());
        return "produtos/form";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Integer id) {
        pratoService.deleteById(id);
        return "redirect:/produtos";
    }
}
