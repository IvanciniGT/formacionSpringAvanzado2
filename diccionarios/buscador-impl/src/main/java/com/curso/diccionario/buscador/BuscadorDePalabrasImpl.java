package com.curso.diccionario.buscador;

import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BuscadorDePalabrasImpl implements BuscadorDePalabras {
    @Override
    public boolean existeElIdioma(@NonNull String idioma){
        return false;
    }

    @Override
    public Optional<List<String>> buscarPalabra(@NonNull String idioma, @NonNull String palabra){
        return null;
    }
}
