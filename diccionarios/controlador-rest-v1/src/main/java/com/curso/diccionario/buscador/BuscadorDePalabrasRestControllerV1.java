package com.curso.diccionario.buscador;

import com.curso.diccionario.buscador.dto.BusquedaPalabra;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<String>> buscarPalabraQuery(@RequestParam(required = true) String idioma,
                                                           @RequestParam(required = true) String palabra){
        return buscarPalabras(idioma, palabra);
    }
    /*
    {
        "idioma": "ES",
        "palabra": "manzana"
    }
    */
    @GetMapping("/buscarPalabra2")
    //https://miservidor/api/v1/buscarPalabra2 // Pero los datos van en un JSON
    public ResponseEntity<List<String>> buscarPalabraBody(@RequestBody BusquedaPalabra datosDeLaBusqueda){
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