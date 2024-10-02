package com.curso.diccionario.buscador;

import com.curso.diccionario.AplicacionDePrueba;
import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.entity.Palabra;
import com.curso.diccionario.entity.Significado;
import com.curso.diccionario.repository.RepositorioIdiomas;
import com.curso.diccionario.repository.RepositorioPalabras;
import com.curso.diccionario.repository.RepositorioSignificados;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AplicacionDePrueba.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuscadorDePalabrasIntegrationTest {

    public static final String PALABRA_EXISTENTE_CON_UN_SIGNIFICADO = "manzana";
    public static final String PALABRA_NO_EXISTENTE = "archilococo";
    public static final String IDIOMA_EXISTENTE = "ES";
    public static final String IDIOMA_NO_EXISTENTE = "de los elfos";
    public static final String SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO = "Fruto del manzano";
    private final RepositorioIdiomas miRepositorio;
    private final RepositorioPalabras miRepositorioPalabras;
    private final RepositorioSignificados miRepositorioSignificados;
    private final BuscadorDePalabras miBuscadorDePalabras;

    @Autowired
    BuscadorDePalabrasIntegrationTest(RepositorioIdiomas miRepositorio,
                                      BuscadorDePalabras miBuscadorDePalabras,
                                      RepositorioPalabras miRepositorioPalabras,
                                      RepositorioSignificados miRepositorioSignificados
    ){ // Solicito la dependencia (inyección de dependencias)
        this.miRepositorio = miRepositorio;
        this.miBuscadorDePalabras = miBuscadorDePalabras;
        this.miRepositorioSignificados = miRepositorioSignificados;
        this.miRepositorioPalabras = miRepositorioPalabras;
    }

    @BeforeAll
    void crearIdiomaEnBBDD(){
        Idioma idioma = Idioma.builder().nombre(IDIOMA_EXISTENTE).build();
        miRepositorio.save(idioma);

        Palabra melon = Palabra.builder().palabra("melón").idioma(idioma).build();
        miRepositorioPalabras.save(melon);
        miRepositorioSignificados.save(Significado.builder().significado("Fruto del melonero").palabra(melon).build());
        miRepositorioSignificados.save(Significado.builder().significado("Persona con pocas luces").palabra(melon).build());

        Palabra manzana = Palabra.builder().palabra(PALABRA_EXISTENTE_CON_UN_SIGNIFICADO).idioma(idioma).build();
        miRepositorioPalabras.save(melon);
        miRepositorioSignificados.save(Significado.builder().significado(SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO).palabra(manzana).build());
    }

    @DisplayName("Preguntar por un idioma que existe")
    void preguntarPorIdiomaExistente() {
        boolean existe = miBuscadorDePalabras.existeElIdioma(IDIOMA_EXISTENTE);
        assertTrue(existe);
    }

    @DisplayName("Preguntar por un idioma que existe pero con case distinto")
    void preguntarPorIdiomaExistenteConCaseDistinto() {
        boolean existe = miBuscadorDePalabras.existeElIdioma("es");
        assertTrue(existe);
    }

    @DisplayName("Preguntar por un idioma que existe")
    void preguntarPorIdiomaNull() {
        assertThrows( NullPointerException.class, () -> miBuscadorDePalabras.existeElIdioma(null));
    }

    @Test
    @DisplayName("Preguntar por un idioma que no existe")
    void preguntarPorIdiomaNoExistente() {
        boolean existe = miBuscadorDePalabras.existeElIdioma("De los elfos");
        assertFalse(existe);
    }

    @Test
    @DisplayName("Preguntar por una palabra que no existe")
    void preguntarPorPalabraNoExistente(){
        Optional<List<String>> potencialesSignificados = miBuscadorDePalabras.buscarPalabra(IDIOMA_EXISTENTE, PALABRA_NO_EXISTENTE);
        assertTrue(potencialesSignificados.isEmpty());
    }
    @Test
    @DisplayName("Preguntar por una palabra sin pasar idioma")
    void preguntarPorPalabraIdiomaNull(){
        assertThrows( NullPointerException.class, () -> miBuscadorDePalabras.buscarPalabra(null, PALABRA_NO_EXISTENTE));
    }
    @Test
    @DisplayName("Preguntar por una palabra null")
    void preguntarPorPalabraNull(){
        assertThrows( NullPointerException.class, () -> miBuscadorDePalabras.buscarPalabra(IDIOMA_EXISTENTE,null));
    }

    @Test
    @DisplayName("Preguntar por una palabra de un idioma que no existe")
    void preguntarPorPalabraDeIdiomaNoExistente(){
        Optional<List<String>> potencialesSignificados = miBuscadorDePalabras.buscarPalabra(IDIOMA_NO_EXISTENTE, PALABRA_NO_EXISTENTE);
        assertTrue(potencialesSignificados.isEmpty());
    }

    @Test
    @DisplayName("Preguntar por una palabra que existe")
    void preguntarPorPalabraExistente(){
        Optional<List<String>> potencialesSignificados = miBuscadorDePalabras.buscarPalabra(IDIOMA_EXISTENTE,
                PALABRA_EXISTENTE_CON_UN_SIGNIFICADO);
        assertTrue(potencialesSignificados.isPresent());
        assertEquals(1, potencialesSignificados.get().size());
        assertEquals(SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO, potencialesSignificados.get().get(0));
    }

    @Test
    @DisplayName("Preguntar por una palabra que existe con case distinto")
    void preguntarPorPalabraExistenteConCaseDistinto(){
        Optional<List<String>> potencialesSignificados = miBuscadorDePalabras.buscarPalabra(IDIOMA_EXISTENTE,
                PALABRA_EXISTENTE_CON_UN_SIGNIFICADO.toUpperCase());
        assertTrue(potencialesSignificados.isPresent());
        assertEquals(1, potencialesSignificados.get().size());
        assertEquals(SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO, potencialesSignificados.get().get(0));
    }

}
