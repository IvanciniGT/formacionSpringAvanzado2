package com.curso.diccionario.buscador;

import com.curso.diccionario.AplicacionDePrueba;
import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.entity.Palabra;
import com.curso.diccionario.entity.Significado;
import com.curso.diccionario.repository.RepositorioIdiomas;
import com.curso.diccionario.repository.RepositorioPalabras;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AplicacionDePrueba.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BuscadorDePalabrasTest {

    public static final String PALABRA_EXISTENTE_CON_UN_SIGNIFICADO = "manzana";
    public static final String PALABRA_NO_EXISTENTE = "archilococo";
    public static final String IDIOMA_EXISTENTE = "ES";
    public static final String IDIOMA_NO_EXISTENTE = "de los elfos";
    public static final String SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO = "Fruto del manzano";
    @MockBean // Esto hace 2 cosas
    // Crea (mediante una librería llamada Mockito una implementación DUMMY de esa interfaz
    // Le dice a Spring que entregue una instancia de esa clase que va a crear Mockito cuando alguien pida un RepositorioIdiomas
    // Lo mismo que habíamos hecho nosotros a mano al poner @Repository @Primary
    private RepositorioIdiomas miRepositorioIdiomas;

    @Captor
    private ArgumentCaptor<String> nombreDelIdiomaSolicitadoAlRepositorio;

    @MockBean
    private RepositorioPalabras miRepositorioPalabras;
    private final BuscadorDePalabrasImpl miBuscadorDePalabras;

    @Autowired
    BuscadorDePalabrasTest(BuscadorDePalabrasImpl miBuscadorDePalabras ){ // Solicito la dependencia (inyección de dependencias)
        this.miBuscadorDePalabras = miBuscadorDePalabras;
    }

    @BeforeAll
    void crearIdiomaEnBBDD(){

        Idioma idiomaExistente = Idioma.builder().nombre(IDIOMA_EXISTENTE).id(12L).build();
        Palabra manzana = Palabra.builder().palabra("manzana").idioma(idiomaExistente).id(19L).build();

        when(miRepositorioIdiomas.findByNombre(IDIOMA_EXISTENTE)).thenReturn(Optional.of(idiomaExistente));

        when(miRepositorioIdiomas.findByNombre(null)).thenThrow(NullPointerException.class);

        when(miRepositorioPalabras.findByPalabraAndIdioma(null,idiomaExistente)).thenThrow(NullPointerException.class);
        when(miRepositorioPalabras.findByPalabraAndIdioma(PALABRA_EXISTENTE_CON_UN_SIGNIFICADO, null)).thenThrow(NullPointerException.class);

        when(miRepositorioPalabras.findByPalabraAndIdioma(PALABRA_EXISTENTE_CON_UN_SIGNIFICADO, idiomaExistente))
                .thenReturn(Optional.of(manzana));

        manzana.setSignificados(List.of(Significado.builder().id(111L).significado(SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO).palabra(manzana).build()));
    }

    @DisplayName("Preguntar por un idioma que existe")
    void preguntarPorIdiomaExistente() {
        boolean existe = miBuscadorDePalabras.existeElIdioma(IDIOMA_EXISTENTE);
        assertTrue(existe);
        // Que me falta por comprobar?
        // El buscador debería llamar al repositorio y pasarle el mismo idioma que esté recibiendo
        // Lo que hemos montado no es un MOCK... es un STUB... un código que siempre devuelve lo mismo... CARTON-PIEDRA
        // Y nos hace falta un SPY, que es otro tipo de objeto de pruebas. Alguien que cuando es llamado anota que ha sido llamado y con los datos que ha sido llamado
        // Para yo poder preguntarle luego.. OYE! te han llamado? Y con que datos? = SPY
        // De hecho, lo que queremos en este caso es un STUB + SPY = MOCK
        verify(miRepositorioIdiomas).findByNombre(nombreDelIdiomaSolicitadoAlRepositorio.capture());
        // MOCKITO, verifica que se ha llamado a la función findByNombre del repo ese de cartón piedra que hemos montado.
        // Te habrán pasado el nombre de un idioma.. capturalo
        assertEquals(IDIOMA_EXISTENTE, nombreDelIdiomaSolicitadoAlRepositorio.getValue());
    } // FIRST: S=Self-validating

    /*

    public class MiBuscadorDePalabras implements BuscadorDePalabras { // ESTO ES EL CODIGO REAL QUE HAN IMPLEMENTADO A VER SI SUBE A PRO
        boolean existeElIdioma(@NonNull String idioma){
            return true;
        }
        Optional<List<String>> buscarPalabra(@NonNull String idioma, @NonNull String palabra);{ // ESTA GUAY }
    }
     */

    @DisplayName("Preguntar por un idioma que existe pero con case distinto")
    void preguntarPorIdiomaExistenteConCaseDistinto() {
        boolean existe = miBuscadorDePalabras.existeElIdioma(IDIOMA_EXISTENTE.toLowerCase());
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
        verify(miRepositorioIdiomas).findByNombre(nombreDelIdiomaSolicitadoAlRepositorio.capture());
        assertEquals("De los elfos", nombreDelIdiomaSolicitadoAlRepositorio.getValue());
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
