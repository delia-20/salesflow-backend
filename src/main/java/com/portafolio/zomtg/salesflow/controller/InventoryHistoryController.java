package com.portafolio.zomtg.salesflow.controller;

import com.portafolio.zomtg.salesflow.model.entities.InventoryMovement;
import com.portafolio.zomtg.salesflow.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/dashboard/history/")
public class InventoryHistoryController {
    private InventoryService inventoryService;

    @Autowired
    public InventoryHistoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("product/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") UUID productId, Authentication authentication){
        String username=authentication.getName();
        System.out.println("username:"+username);
        List<InventoryMovement> movements= inventoryService.getInventoryMovements(username,productId);
        return ResponseEntity.status(HttpStatus.OK).body(movements);
    }



}
