package com.curso.diccionario.buscador.bdd;

import com.curso.diccionario.AplicacionDePrueba;
import com.curso.diccionario.entity.Idioma;
import com.curso.diccionario.entity.Palabra;
import com.curso.diccionario.entity.Significado;
import com.curso.diccionario.repository.RepositorioIdiomas;
import com.curso.diccionario.repository.RepositorioPalabras;
import com.curso.diccionario.repository.RepositorioSignificados;
import io.cucumber.java.en.When;
import io.cucumber.java.es.Cuando;
import io.cucumber.java.es.Dado;
import io.cucumber.java.es.Entonces;
import io.cucumber.spring.CucumberContextConfiguration;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Suite // Le indico a JUnit que esto son pruebas que el puede ejecutar.

@IncludeEngines("cucumber") // Son test de cucumber... y podemos poner ese valor "cucumber" por que tenemos la dependencia de "cucumber-junit-platform"
@SelectClasspathResource("features") // Oye, pasale al motor de pruebas estos ficheros, que le hacen falta

@SpringBootTest(classes = AplicacionDePrueba.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// No se en que puerto.. No controlo el entorno donde se ejecutará. Uno que puedas!
@AutoConfigureMockMvc // Spring, montame un clienteHTTP de pruebas conectado con ese tomcat que has levantao!
@CucumberContextConfiguration // Cucumber, estamos usando Spring... Pídele a el cosas si te hacen falta. Que te rellene los autowired.
@WithMockUser(username = "federico", roles = {"ADMIN"})

public class MicroservicioBuscarPalabrasV1_BDDTest {

    @Autowired
    private MockMvc clienteHttp; // Ese cliente que has montao, pásamelo!
    @Autowired
    private RepositorioIdiomas miRepositorio;
    @Autowired
    private RepositorioPalabras miRepositorioPalabras;
    @Autowired
    private RepositorioSignificados miRepositorioSignificados;

    private Idioma idioma;
    private Palabra palabra;
    private JSONObject cuerpoPeticion;
    private String metodo;
    private String endPoint;
    private ResultActions resultado;

    @Dado("Tengo un sistema operativo, con su controlador, servicio y repositorio")
    public void tengo_un_sistema_operativo_con_su_controlador_servicio_y_repositorio() {
        //VALE ! Lo tengo por ahi arriba escrito.
    }

    @Dado("en el repositorio tengo dado de alta el idioma {string}")
    public void en_el_repositorio_tengo_dado_de_alta_el_idioma(String nombreIdioma) {
        if(miRepositorio.findByNombre(nombreIdioma).isEmpty()){
            idioma = Idioma.builder().nombre(nombreIdioma).build();
            idioma = miRepositorio.save(idioma);
        }
    }

    @Dado("en el repositorio tengo dada de alta la palabra {string} para el idioma anterior")
    public void en_el_repositoprio_tengo_dada_de_alta_la_palabra_para_el_idioma_anterior(String textoPalabra) {
        palabra = Palabra.builder().palabra(textoPalabra).idioma(idioma).build();
        palabra = miRepositorioPalabras.save(palabra);
    }

    @Dado("en el repositorio tengo dado de alta el significado {string} para la palabra anterior")
    public void en_el_repositorio_tengo_dado_de_alta_el_significado_para_la_palabra_anterior(String significado) {
        miRepositorioSignificados.save(Significado.builder().significado(significado).palabra(palabra).build());
    }

    @Dado("que tengo un Objeto JSON")
    public void que_tengo_un_objeto_json() {
        cuerpoPeticion = new JSONObject();
    }

    @Dado("que ese objeto tiene una propiedad {string} con valor {string}")
    public void que_ese_objeto_tiene_una_propiedad_con_valor(String propiedad, String valor) throws JSONException {
        cuerpoPeticion.put(propiedad,valor);
    }

    @Cuando("realizo una petición http {string} al endpoint {string}")
    public void realizo_una_petición_http_get_al_endpoint(String metodo, String endpoint) {
        this.metodo = metodo;
        this.endPoint = endpoint;
    }

    @When("envío ese objeto JSON en el cuerpo de la petición")
    public void envío_ese_objeto_json_en_el_cuerpo_de_la_petición() {
        // VALE
    }

    @Entonces("obtengo una respuesta HTTP")
    public void obtengo_una_respuesta_http() throws Exception {
        MockHttpServletRequestBuilder peticion = null;
        switch(metodo.toUpperCase()){
            case "GET":
                peticion = MockMvcRequestBuilders.get(endPoint);
                break;
            case "POST":
                peticion = MockMvcRequestBuilders.post(endPoint);
                break;
            case "PUT":
                peticion = MockMvcRequestBuilders.put(endPoint);
                break;
            case "DELETE":
                peticion = MockMvcRequestBuilders.delete(endPoint);
                break;
            default:
                throw new Exception("método http no soportado");
        }
        if(cuerpoPeticion != null){
            peticion = peticion.contentType(MediaType.APPLICATION_JSON)
                .content(cuerpoPeticion.toString());
        }
        resultado = clienteHttp.perform( peticion );
    }

    @Entonces("esa respuesta tiene por código de estado {string}")
    public void esa_respuesta_tiene_por_código_de_estado(String respuesta) throws Exception {
        switch(respuesta.toUpperCase()) {
            case "OK":
                resultado.andExpect(status().isOk());
                break;
            case "NO ENCONTRADO":
                resultado.andExpect(status().isNotFound());
                break;
            default:
                throw new Exception("código de respuesta no soportado");
        }
    }

    @Entonces("el cuerpo de esa respuesta tiene un objeto JSON")
    public void el_cuerpo_de_esa_respuesta_tiene_un_objeto_json() throws Exception {
        resultado.andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Entonces("ese objeto JSON contiene una lista elemento en la posición {int} es {string}")
    public void ese_objeto_json_contiene_una_lista_elemento_en_la_posición_es(Integer posicion, String valor) throws Exception {
        resultado.andExpect(jsonPath("$.["+(posicion-1)+"]").value(valor));
    }

}
