package com.franciscoleon.blog.blog_francisco_leon.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja una HttpMessageNotReadableException, que se produce 
     * cuando el formato JSON es inválido o el tipo de dato es incorrecto.
     * 
     * @param ex la excepción que se produce
     * @return una respuesta HTTP con un status 400 (Bad Request) 
     *         que incluye el mensaje "Formato JSON inválido o tipo de dato incorrecto".
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseError(HttpMessageNotReadableException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("message", "Formato JSON inválido o tipo de dato incorrecto");
        body.put("error", ex.getLocalizedMessage());
        return ResponseEntity.badRequest().body(body);
    }

}

