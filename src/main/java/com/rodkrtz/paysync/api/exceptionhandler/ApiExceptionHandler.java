package com.rodkrtz.paysync.api.exceptionhandler;

import com.rodkrtz.paysync.domain.exception.ContaNaoEncontradaException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<?> handleContaNaoEncontrada(ContaNaoEncontradaException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Conta não encontrada");
        body.put("message", ex.getMessage());
        body.put("path", ServletUriComponentsBuilder.fromCurrentRequest().toUriString());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

}
