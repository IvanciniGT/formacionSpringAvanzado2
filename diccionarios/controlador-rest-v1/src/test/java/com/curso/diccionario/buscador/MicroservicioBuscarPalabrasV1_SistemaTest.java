package com.curso.diccionario.buscador;

import com.curso.diccionario.AplicacionDePrueba;
import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.entity.Palabra;
import com.curso.diccionario.entity.Significado;
import com.curso.diccionario.repository.RepositorioIdiomas;
import com.curso.diccionario.repository.RepositorioPalabras;
import com.curso.diccionario.repository.RepositorioSignificados;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
// Que arranca esto? Toda la mierda que hemos definido.. y que más? UN TOMCAT, ya que mi app contiene un RestController
@SpringBootTest(classes = AplicacionDePrueba.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
                                                    // No se en que puerto.. No controlo el entorno donde se ejcutará. Uno que puedas!

@AutoConfigureMockMvc // Spring, montame un clienteHTTP de pruebas conectado con ese tomcat que has levantao!
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MicroservicioBuscarPalabrasV1_SistemaTest {

    private final MockMvc clienteHttp; // Ese cliente que has montao, pásamelo!
    public static final String PALABRA_EXISTENTE_CON_UN_SIGNIFICADO = "manzana";
    public static final String PALABRA_NO_EXISTENTE = "archilococo";
    public static final String IDIOMA_EXISTENTE = "ES";
    public static final String IDIOMA_NO_EXISTENTE = "de los elfos";
    public static final String SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO = "Fruto del manzano";
    private final RepositorioIdiomas miRepositorio;
    private final RepositorioPalabras miRepositorioPalabras;
    private final RepositorioSignificados miRepositorioSignificados;

    @Autowired
    public MicroservicioBuscarPalabrasV1_SistemaTest(MockMvc clienteHttp,
                                                     RepositorioIdiomas miRepositorio,
                                                     RepositorioPalabras miRepositorioPalabras,
                                                     RepositorioSignificados miRepositorioSignificados){
        this.clienteHttp = clienteHttp;
        this.miRepositorio = miRepositorio;
        this.miRepositorioPalabras = miRepositorioPalabras;
        this.miRepositorioSignificados = miRepositorioSignificados;
    }

    @BeforeAll
    void crearIdiomaEnBBDD(){
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

    @Test
    @DisplayName("Buscar palabra existente en un idioma existente")
    @WithMockUser(username = "federico", roles = {"ADMIN"})
    void test1() throws Exception {

        // Dado
        //  que tengo un controlador de estos √
        //  y un servidor de apps   √
        //  que tengo un servicio de BuscadorPalabras guay      √
        //  y que tengo un repositorio guay √
        //  y el repositorio tiene cargado la palabra manzana en idioma español, con significados Fruto del manzano √
        // Cuando
        //  me llaman al endpoint /api/v1/buscarPalabra con idioma "es" y palabra "perro" √
        // Entonces
        //  me devuelve un HTTP Response con el código 200 y en el cuerpo del request un JSON con [Fruto del manzano] √
        JSONObject cuerpoPeticion = new JSONObject();
        cuerpoPeticion.put("idioma",IDIOMA_EXISTENTE);
        cuerpoPeticion.put("palabra",PALABRA_EXISTENTE_CON_UN_SIGNIFICADO);

        ResultActions resultado = clienteHttp.perform(
                MockMvcRequestBuilders.get("/api/v1/buscarPalabra2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoPeticion.toString())
        );

        resultado.andExpect(status().isOk());
        resultado.andExpect(content().contentType(MediaType.APPLICATION_JSON));
        //JSONObject resultadoJson = new JSONObject(resultado.andReturn().getResponse().getContentAsString());
        resultado.andExpect(jsonPath("$.[0]").value(SIGNIFICADO_PALABRA_CON_UN_SIGNIFICADO));
        // https://jsonpath.com/
    }

    @Test
    @DisplayName("Buscar palabra no existente en un idioma existente")
    @WithMockUser(username = "federico", roles = {"ADMIN"})
    void test2() throws Exception {

        JSONObject cuerpoPeticion = new JSONObject();
        cuerpoPeticion.put("idioma",IDIOMA_EXISTENTE);
        cuerpoPeticion.put("palabra",PALABRA_NO_EXISTENTE);

        ResultActions resultado = clienteHttp.perform(
                MockMvcRequestBuilders.get("/api/v1/buscarPalabra2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoPeticion.toString())
        );

        resultado.andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Buscar palabra existente en un idioma no existente")
    @WithMockUser(username = "federico", roles = {"ADMIN"})
    void test3() throws Exception {
        JSONObject cuerpoPeticion = new JSONObject();
        cuerpoPeticion.put("idioma",IDIOMA_NO_EXISTENTE);
        cuerpoPeticion.put("palabra",PALABRA_EXISTENTE_CON_UN_SIGNIFICADO);

        ResultActions resultado = clienteHttp.perform(
                MockMvcRequestBuilders.get("/api/v1/buscarPalabra2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cuerpoPeticion.toString())
        );
        resultado.andExpect(status().isNotFound());
    }

}
