package com.ghost.restaurante_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ghost.restaurante_springboot.Controllers.ControllerMain;

@SpringBootApplication
public class RestauranteSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestauranteSpringbootApplication.class, args);
		ControllerMain con = new ControllerMain();
		con.main();
	}

}
