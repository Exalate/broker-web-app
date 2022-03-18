package com.example.externalapi.controller;

import com.example.externalapi.exception.ElementIsUsed;
import com.example.externalapi.exception.IncorrectOperation;
import com.example.externalapi.exception.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;

@RestControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Clarification> handleNotFoundException(Exception exception) {
        return new ResponseEntity<>(new Clarification(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<Clarification> handleEntityExistsException(Exception exception) {
        return new ResponseEntity<>(new Clarification(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectOperation.class)
    protected ResponseEntity<Clarification> handleIncorrectOperation(Exception exception) {
        return new ResponseEntity<>(new Clarification(exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(ElementIsUsed.class)
    protected ResponseEntity<Clarification> handleElementIsUsed(Exception exception) {
        return new ResponseEntity<>(new Clarification(exception.getMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @Data
    @AllArgsConstructor
    private static class Clarification {
        private String message;
    }

//    @ExceptionHandler(NotFoundException.class)
//    public void handleNotFoundException(Exception exception, HttpServletResponse response) throws IOException {
//        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
//    }
}
