package com.curso.diccionario;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Mira to-do lo que hay en este paquete y SUBPAQUETES
// Ahora me va a encontrar mis repos y mis entidades, para hacerse cargo de ellas el Spring.
public class MicroservicioBusquedaPalabras {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioBusquedaPalabras.class, args); // Inversión de control
    }
}
