package com.curso.diccionario.buscador;

import com.curso.diccionario.buscador.dto.BusquedaPalabra;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
// Esta anotación no es solo SEMANTICA.. Extiende de component
@RequiredArgsConstructor
public class BuscadorDePalabrasRestControllerV1 {

    private final BuscadorDePalabras buscador;

    @RequestMapping(value = "/idioma/{idioma}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> existeElIdioma(@PathVariable("idioma") String idioma){
        return buscador.existeElIdioma(idioma)
                        ? ResponseEntity.ok().build()
                        : ResponseEntity.notFound().build();
    }

    @GetMapping("/idioma/{idioma}/{palabra}")
    //http://miservidor/api/v1/idioma/ES/manzana
    public ResponseEntity<List<String>> buscarPalabraUrl(@PathVariable("idioma") String idioma,@PathVariable("palabra") String palabra){
        return buscarPalabras(idioma, palabra);
    }

    //@GetMapping("/buscarPalabra") // Me pasan como PARAMETROS DE LA URL: Idioma y Palabra
    @RequestMapping(value = "/buscarPalabra", method = RequestMethod.GET)
    //http://miservidor/api/v1/buscarPalabra?idioma=ES&palabra=manzana
    public ResponseEntity<List<String>> buscarPalabraQuery(@RequestParam(required = false) String idioma,
                                                           @RequestParam(required = false) String palabra){
        return buscarPalabras(idioma, palabra);
    }
    /*
    {
        "idioma": "ES",
        "palabra": "manzana"
    }
    */

    //@PreAuthorize("isAuthenticated()") // Aqui dentro de usa un lenguaje propio de Spring SPEL.
    //@PreAuthorize("hasRole('ADMIN')") // Estas anotaciones Pre y Post hay que habilitarlas
    //@PostAuthorize() // Y cuando se devuelvan los datos quiero que solo si el expediente que estoy devolviendo tiene un campo propietario igual al id del usuario que está haciendo la petición.
    //@Secured("ADMIN")
    @RolesAllowed("ADMIN") // JSR250 hay es donde se definen estas anotaciones.
    @GetMapping("/buscarPalabra2")
    //https://miservidor/api/v1/buscarPalabra2 // Pero los datos van en un JSON
    public ResponseEntity<List<String>> buscarPalabraBody(@RequestBody BusquedaPalabra datosDeLaBusqueda){
        return buscarPalabras(datosDeLaBusqueda.getIdioma(), datosDeLaBusqueda.getPalabra());
    }

    @RolesAllowed("ADMIN") // JSR250 hay es donde se definen estas anotaciones.
    @PostMapping("/buscarPalabra2")
    //https://miservidor/api/v1/buscarPalabra2 // Pero los datos van en un JSON
    public ResponseEntity<List<String>> buscarPalabra3Body(@RequestBody BusquedaPalabra datosDeLaBusqueda){
        // Lo suyo es que antes de solicitar al servicio hiciera yo aqui una validación de los datos.
        // Podría quitarla pues del Servicio? NI DE COÑA.. Allí tiene que estar. Aquí puedo hacer una de cortesía.
        // Esto sería lo suyo.. Pero yo lo voy a hacer de otra forma (que no es como YO LO HARIA EN LA REALIDAD)
        // Pero que para otros casos SI NOS INTERESA y lo que quiero es enseñaros la funcionalidad.
        return buscarPalabras(datosDeLaBusqueda.getIdioma(), datosDeLaBusqueda.getPalabra());
    }

    private ResponseEntity<List<String>> buscarPalabras(String idioma, String palabra){
        Optional<List<String>> significados = buscador.buscarPalabra(idioma, palabra);
/*
        if(significados.isPresent()){
            return ResponseEntity.ok(significados.get());
        }else{
            return ResponseEntity.notFound().build();
        }
*/
        return significados.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

}

// http://miservidor/api/v1/idioma/ES <- GET -> existeElIdioma
// GET:     Obtener datos del servidor
// POST:    Postear datos: NUEVOS DATOS
// PUT:     Modificar datos
// DELETE:  Borrar datos
// HEAD:    Comprobando datos? Si existen?