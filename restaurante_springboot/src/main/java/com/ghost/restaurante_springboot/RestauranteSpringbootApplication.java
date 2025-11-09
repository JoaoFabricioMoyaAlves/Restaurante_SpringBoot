package com.ghost.restaurante_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// IMPORTAÇÃO NÃO É NECESSÁRIA AQUI, MAS PODE SER MANTIDA
// import com.ghost.restaurante_springboot.Controllers.ControllerMain; 

@SpringBootApplication
public class RestauranteSpringbootApplication {

    public static void main(String[] args) {
        // CORRETO: Inicia o contêiner do Spring
        SpringApplication.run(RestauranteSpringbootApplication.class, args);
        
        // REMOVER ESSAS DUAS LINHAS:
        // ControllerMain con = new ControllerMain(); 
        // con.viewDashboard(); 
    }
}