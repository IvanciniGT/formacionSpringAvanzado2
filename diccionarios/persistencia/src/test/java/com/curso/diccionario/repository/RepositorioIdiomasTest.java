package com.curso.diccionario.repository;

import com.curso.diccionario.entity.Idioma;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // JUNIT: Que sepas que puede pedir argumentos a otros en el constructor,
                                   // si te hace falta
                                   // A quién? A Spring
class RepositorioIdiomasTest {

    private final RepositorioIdiomas miRepositorio;

                            // Y este es el único caso legítimo de uso de Autowired.
    RepositorioIdiomasTest(@Autowired RepositorioIdiomas miRepositorio){ // Solicito la dependencia (inyección de dependencias)
        this.miRepositorio = miRepositorio;
    }

    @Test
    @DisplayName("Asegurar que tengo una función para buscar por nombre de idioma")
    void testBusquedaPorNombreDeIdioma(){
        // Quiero poder buscar por Idioma ES
        Optional<Idioma> idioma=miRepositorio.findByNombre("ES");
        // Y la función me debe devolver el idioma
        assertTrue(idioma.isPresent());
        assertEquals("ES", idioma.get().getNombre());
    }

    @Test
    @DisplayName("Asegurar que tengo una función para buscar los idiomas cuyo nombre comiencen por un prefijo")
    void testBusquedaPorPrefijoDeNombre(){
        // Quiero poder buscar por Idioma ES
        Optional<List<Idioma>> idiomas=miRepositorio.findByNombreStartingWith("ES");
        // Y la función me debe devolver el idioma
        assertTrue(idiomas.isPresent());
        assertEquals(1, idiomas.get().size() );
        assertEquals("ES", idiomas.get().get(0).getNombre());
    }

}