package com.portafolio.zomtg.salesflow.controller;

import com.portafolio.zomtg.salesflow.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NoEnoughStockException.class)
    public ResponseEntity<?> handleStock(NoEnoughStockException ex){
        Map<String,String> map = new HashMap<>();
        map.put("message",ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(map);
    }

    @ExceptionHandler(ProductNoFound.class)
    public ResponseEntity<?> handleProductNoFound(ProductNoFound ex){
        Map<String,String> map = new HashMap<>();
        map.put("message",ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(map);
    }

    @ExceptionHandler(InvalidCredentials.class)
    public ResponseEntity<?> handleInvalidCredentials(InvalidCredentials ex){
        Map<String,String> map = new HashMap<>();
        map.put("message",ex.getMessage());
        return  ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(map);
    }

    @ExceptionHandler(SystemInterErrorException.class)
    public ResponseEntity<?> handleSystemInterError(SystemInterErrorException ex){
        Map<String,String> map = new HashMap<>();
        map.put("message",ex.getMessage());
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
    }

    @ExceptionHandler(BusinessOperationException.class)
    public ResponseEntity<?> handleSuccessOperation(BusinessOperationException ex){
        Map<String,String> map = new HashMap<>();
        map.put("message",ex.getMessage());
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(map);

    }
    @ExceptionHandler(ObjectNotFound.class)
    public ResponseEntity<?> handleObjectNotFound(ObjectNotFound ex){
        Map<String,String> map = new HashMap<>();
        map.put("message",ex.getMessage());
        return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(map);

    }


}
