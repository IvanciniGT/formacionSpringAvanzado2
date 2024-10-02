package com.curso.diccionario.repository;

import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.entity.Palabra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// Tipo de objeto que gestiono
                                                                      // El tipo de su identificador
public interface RepositorioPalabras extends JpaRepository<Palabra, Long> {

    Optional<Palabra> findByPalabraAndIdioma(String palabra, Idioma idioma);

}
