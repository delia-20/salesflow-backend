package com.portafolio.zomtg.salesflow.sales.controller;

import com.portafolio.zomtg.salesflow.sales.dto.SaleResponse;
import com.portafolio.zomtg.salesflow.sales.dto.SalesRequestDTO;
import com.portafolio.zomtg.salesflow.receipt.entity.Receipt;
import com.portafolio.zomtg.salesflow.sales.entity.Sale;
import com.portafolio.zomtg.salesflow.service.OnlineSaleService;
import com.portafolio.zomtg.salesflow.receipt.service.ReceiptService;
import com.portafolio.zomtg.salesflow.sales.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/")
public class SaleController {
    SaleService saleService;
    OnlineSaleService  onlineSaleService;
    @Autowired
    public SaleController(SaleService saleService, ReceiptService receiptService, OnlineSaleService onlineSaleService) {
        this.saleService = saleService;
        this.onlineSaleService = onlineSaleService;
    }

    @PostMapping("sales")
    @PreAuthorize("hasRole('OWNER') or hasRole('EMPLOYEE')")
    public ResponseEntity<?> createSale(@RequestBody SalesRequestDTO request, Authentication authentication) {
        String username = authentication.getName();
        SaleResponse result =saleService.createSale(request,username);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("online/sales")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> onlineSale(@RequestBody SalesRequestDTO request, Authentication authentication) {
        String username = authentication.getName();
        Sale sale=onlineSaleService.createSale(request,username);
        return ResponseEntity.ok().body(sale);
    }

    @PostMapping("sales/{saleId}/completed")
    @PreAuthorize("hasRole('OWNER') or hasRole('EMPLOYEE')  ")
    public ResponseEntity<?> completeSale   (@PathVariable UUID saleId, Authentication authentication) {
        String username = authentication.getName();
        Receipt receipt=saleService.createTicket(saleId,username);
            return ResponseEntity.status(HttpStatus.OK).body(Map.of("ticket",receipt));
    }

    @PostMapping("online/sales/{saleId}/completed")
    @PreAuthorize("hasRole('CUSTOMER')")
    public  ResponseEntity<?> completeOnlineSale (@PathVariable UUID saleId, Authentication authentication) {
        String username = authentication.getName();
        Receipt receipt=onlineSaleService.getTicket(saleId,username);
        return ResponseEntity.status(HttpStatus.OK).body(receipt);

    }

    @PutMapping("sales/{saleId}/cancelled")
    @PreAuthorize("hasRole('OWNER') or hasRole('EMPLOYEE')")
    public ResponseEntity<?> cancelSale  (@PathVariable UUID saleId, Authentication authentication) {
        String username = authentication.getName();
        Sale sale= saleService.cancelledSale(saleId,username);
        return ResponseEntity.status(HttpStatus.OK).body(sale);

    }
    @PutMapping("online/sales/{saleId}/cancelled")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> cancelOnlineSale (@PathVariable UUID saleId, Authentication authentication) {
        String username = authentication.getName();
        Sale sale =onlineSaleService.cancelSale(saleId,username);
        return ResponseEntity.status(HttpStatus.OK).body(sale);
    }


    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("sales/{storeId}")
    public ResponseEntity<?> getSales(@PathVariable UUID storeId, Authentication authentication) {
        String username = authentication.getName();
        List<Sale> sales=saleService.getSales(storeId,username);
        return  ResponseEntity.status(HttpStatus.OK).body(sales);
    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("sales/{saleId}")
    public ResponseEntity<?> deleteSale(@PathVariable UUID saleId, Authentication authentication) {
        String username = authentication.getName();
        Sale sale=saleService.deleteSale(saleId,username);
        return ResponseEntity.status(HttpStatus.OK).body(sale);
    }

}
