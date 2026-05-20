package com.portafolio.zomtg.salesflow.products.controller;

import com.portafolio.zomtg.salesflow.products.exception.NoEnoughStockException;
import com.portafolio.zomtg.salesflow.products.exception.ProductNoFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ProductExceptionHandler {
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
}
