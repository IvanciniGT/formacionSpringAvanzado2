package com.curso.diccionario.buscador;

import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface BuscadorDePalabras {
    boolean existeElIdioma(@NonNull String idioma);

    Optional<List<String>> buscarPalabra(@NonNull String idioma, @NonNull String palabra);
}
