package com.curso.diccionario.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity // Vamos a securizar nuestros endpoints
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true) // Esto no debería de estar aquí. Esto se debe configurar a nivel de APP
//@Configuration
public class SeguridadMicroservicioBusquedaPalabras {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Desactivamos CORS
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }
    // Aqui solemos meter cómo gestionamos la autenticación: IAM Eso ya depende de cada proveedor
}

/*
CORS:
Política de Origenes cruzados.
App web (JS) app.com -> miempresa.com/api

El servidor de backend puede habilitar una lista CORS:: Desde que servidores admito que me hagan petición
El navegador cuando un JS va a hacer una petición a un backend, revisa que el dominio desde el que se sirve el js
está habilitado en la política cors del servidor. Si no, corta la comunicación.
*/