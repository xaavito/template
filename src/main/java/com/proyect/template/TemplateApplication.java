package com.proyect.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Punto de entrada y ejecucion de la aplicacion Template
 * 
 * @author Javier Gonzalez
 *
 */
@SpringBootApplication
@ComponentScan("com.proyect.template") // to scan packages mentioned
@EnableMongoRepositories(basePackages = "com.proyect.template")
public class TemplateApplication {
	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);
	}
}
