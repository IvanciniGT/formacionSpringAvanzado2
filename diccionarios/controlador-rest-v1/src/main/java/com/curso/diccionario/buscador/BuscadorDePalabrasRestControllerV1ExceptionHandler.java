package com.curso.diccionario.buscador;

import com.curso.diccionario.buscador.dto.MensajeError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice(basePackageClasses = BuscadorDePalabrasRestControllerV1.class)
public class BuscadorDePalabrasRestControllerV1ExceptionHandler {

    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class})
    public ResponseEntity<MensajeError> handleNullPointerException(NullPointerException e) {
        return ResponseEntity.badRequest().body(new MensajeError("Debe suministrar idioma y palabra en estas funciones."));
    }

}

