package com.curso.diccionario.repository;

import com.curso.diccionario.AplicacionDePrueba;
import com.curso.diccionario.entity.Idioma;
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

@ExtendWith(SpringExtension.class) // JUNIT: Que sepas que puede pedir argumentos a otros en el constructor,
                                   // si te hace falta
                                   // A quién? A Spring
@SpringBootTest(classes = AplicacionDePrueba.class)        // Arranca la app de pruebas... (porque es de pruebas)
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // Crea solo una instancia de esta clase para ejecutar todas las pruebas
    // Y esto nos permite que la función BeforeAll no tenga que ser estática.
class RepositorioIdiomasTest {

    private final RepositorioIdiomas miRepositorio;

                            // Y este es el único caso legítimo de uso de Autowired.
    RepositorioIdiomasTest(@Autowired RepositorioIdiomas miRepositorio){ // Solicito la dependencia (inyección de dependencias)
        this.miRepositorio = miRepositorio;
    }

    @BeforeAll  // JUNIT, antes de ejecutar los test quiero que hagas algo
                // Los hook BeforeAll y AfterAll debe aplicarse POR DEFECTO sobre funciones static
                // Distinto es con los hook BeforeEach y AfterEach, esos si van sobre funciones a nivel de instancia.
    void crearIdiomaEnBBDD(){
        Idioma idioma = Idioma.builder().nombre("ES").build();
        miRepositorio.save(idioma);
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
    @Test
    @DisplayName("Asegurar que no puedo crear un idioma que esté vacio")
    void testCreacionDeIdiomaVacio(){
        // Quiero poder buscar por Idioma ES
        Idioma idioma = Idioma.builder().build();
        // Y la función me debe devolver el idioma
        assertThrows(Exception.class, () -> miRepositorio.save(idioma));
    }
    @Test
    @DisplayName("Asegurar que no puedo crear un idioma cuyo nombre tenga más de 50 caracteres")
    void testCreacionDeIdiomaConNombreMuyLargo(){
        // Quiero poder buscar por Idioma ES
        Idioma idioma = Idioma.builder().nombre("123456789012345678901234567890123456789012345678901").build();
        // Y la función me debe devolver el idioma
        assertThrows(Exception.class, () -> miRepositorio.save(idioma));
    }
}