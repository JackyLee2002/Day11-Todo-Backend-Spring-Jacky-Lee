package org.example.todoapplication.controller;

import org.example.todoapplication.exception.TodoNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TodoNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ResponseEntity handleIllegalStateException(final TodoNotFoundException ex) {
        return new ResponseEntity<>(ex, HttpStatus.NOT_FOUND);
    }

}
