package com.curso.diccionario.repository;

import com.curso.diccionario.entity.Idioma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// Tipo de objeto que gestiono
                                                                      // El tipo de su identificador
public interface RepositorioIdiomas extends JpaRepository<Idioma, Long> {

    Optional<Idioma> findByNombre(String idioma);

    Optional<List<Idioma>> findByNombreStartingWith(String prefijo);

    // JpaRepository es una interfaz que ofrece Spring. Lleva (declara) todos los métodos CRUD para operar sobre Idiomas
                                                                   // Idioma save(Idioma idioma)
                                                                   // Idioma getById(Long id)
                                                                   // Idioma deleteById(Long id)
    // Como esta clase extiende a JPARepository, en automático si Spring lee esta clase al arrancar, ya crea una clase
    // Que implemente estos métodos y me genera una instancia de la clase.
}
