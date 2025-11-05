package com.ghost.restaurante_springboot.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ControllerMain {
    
@GetMapping("/")
public ModelAndView main() {
    ModelAndView mv = new ModelAndView("index");
    return mv;
}

}
