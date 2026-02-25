package com.nguyenquyen.ecommerce.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(DataNotFoundException.class)
    public String handleDataNotFoundException(DataNotFoundException e) {
        return e.getMessage();
    }
//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
//
//    }

}
