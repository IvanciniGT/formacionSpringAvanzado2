package com.curso.diccionario.cargador;

import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.entity.Palabra;
import com.curso.diccionario.entity.Significado;
import com.curso.diccionario.repository.RepositorioIdiomas;
import com.curso.diccionario.repository.RepositorioPalabras;
import com.curso.diccionario.repository.RepositorioSignificados;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CargadorDeDiccionarios {

    public static final String PALABRA_EXISTENTE_CON_UN_SIGNIFICADO = "manzana";
    public static final String IDIOMA_EXISTENTE = "ES";
    public static final String SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO = "Fruto del manzano";

    private final RepositorioIdiomas miRepositorio;
    private final RepositorioPalabras miRepositorioPalabras;
    private final RepositorioSignificados miRepositorioSignificados;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // Esta función se ejecuta cuando Spring arranca y ha creado todas las beans
        log.info("Procediendo a la carga de palabras");
        cargarPalabrasEIdiomas();
    }

    void cargarPalabrasEIdiomas(){
        Idioma idioma = Idioma.builder().nombre(IDIOMA_EXISTENTE).build();
        idioma = miRepositorio.save(idioma);

        Palabra melon = Palabra.builder().palabra("melón").idioma(idioma).build();
        melon = miRepositorioPalabras.save(melon);
        miRepositorioSignificados.save(Significado.builder().significado("Fruto del melonero").palabra(melon).build());
        miRepositorioSignificados.save(Significado.builder().significado("Persona con pocas luces").palabra(melon).build());

        Palabra manzana = Palabra.builder().palabra(PALABRA_EXISTENTE_CON_UN_SIGNIFICADO).idioma(idioma).build();
        manzana = miRepositorioPalabras.save(manzana);
        miRepositorioSignificados.save(Significado.builder().significado(SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO).palabra(manzana).build());
    }
}
