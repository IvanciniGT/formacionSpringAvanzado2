package com.curso.diccionario.buscador;

import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.entity.Palabra;
import com.curso.diccionario.entity.Significado;
import com.curso.diccionario.repository.RepositorioIdiomas;
import com.curso.diccionario.repository.RepositorioPalabras;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BuscadorDePalabrasImpl implements BuscadorDePalabras {

    private final RepositorioIdiomas repositorioIdiomas;
    private final RepositorioPalabras repositorioPalabras;

    @Override
    public boolean existeElIdioma(@NonNull String idioma){
        Optional<Idioma> potencialIdioma = repositorioIdiomas.findByNombre(normalizarIdioma(idioma));
        return potencialIdioma.isPresent();
    }

    @Override
    //@Transactional Asegurar que se abre una conexión... y que queda abierta para esta función
    // Otra alternativa para resolver el problema es Tan pronto como carguemos la palabra, traer los significados
    public Optional<List<String>> buscarPalabra(@NonNull String idioma, @NonNull String palabra){
        Optional<Idioma> potencialIdioma = repositorioIdiomas.findByNombre(normalizarIdioma(idioma));
        if(potencialIdioma.isPresent()){
            Optional<Palabra> potencialPalabra = repositorioPalabras.findByPalabraAndIdioma(normalizarPalabra(palabra), potencialIdioma.get());
            if(potencialPalabra.isPresent()){
                /*
                List<Significado> significados = potencialPalabra.get().getSignificados();
                List<String> significadosComoTexto = new ArrayList<String>();
                for(Significado significado: significados){
                    significadosComoTexto.add(significado.getSignificado());
                }
                return Optional.of(significadosComoTexto);
                */
                return Optional.of(
                potencialPalabra.get().getSignificados()
                                .stream()
                                .map(Significado::getSignificado)
                                .collect(Collectors.toList())
                );
            }
        }
        return Optional.empty();
    }

    private static String normalizarIdioma(String idioma){
        return idioma.toUpperCase();
    }
    private static String normalizarPalabra(String idioma){
        return idioma.toLowerCase();
    }
}
