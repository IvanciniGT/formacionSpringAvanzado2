package com.curso.diccionario;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication // Mira to-do lo que hay en este paquete y SUBPAQUETES
// Ahora me va a encontrar mis repos y mis entidades, para hacerse cargo de ellas el Spring.
@EnableWebSecurity // Vamos a securizar nuestros endpoints
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) // Esto no debería de estar aquí. Esto se debe configurar a nivel de APP
public class AplicacionDePrueba {
}
