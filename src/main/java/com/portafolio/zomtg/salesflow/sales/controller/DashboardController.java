package com.portafolio.zomtg.salesflow.sales.controller;

import com.portafolio.zomtg.salesflow.sales.dto.RequestDTO;
import com.portafolio.zomtg.salesflow.sales.dto.WeeklySalesDto;
import com.portafolio.zomtg.salesflow.sales.entity.Sale;
import com.portafolio.zomtg.salesflow.sales.service.ReportSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("api/v1/owner/dashboard")
public class DashboardController {

    ReportSaleService reportSaleService;
    @Autowired
    public DashboardController(ReportSaleService reportSaleService) {
        this.reportSaleService = reportSaleService;
    }

    @GetMapping("sales/day/{storeId}")
    public ResponseEntity<?> getSales(@PathVariable UUID storeId, Authentication authentication, @RequestBody RequestDTO request){
        LocalDate day=request.getDay();
        String username=authentication.getName();
        List<Sale> sales=reportSaleService.salesPerDay(storeId,username,day);
        return ResponseEntity.status(HttpStatus.OK).body(sales);
    }

    @GetMapping("sales/week/{storeId}")
    public ResponseEntity<?> getSalesWeek(@PathVariable UUID storeId, Authentication authentication, @RequestBody RequestDTO request){
        LocalDate day=request.getDay();
        String username=authentication.getName();
        List<Sale>  sales=reportSaleService.getSalesByWeek(username,storeId,day);
        return ResponseEntity.status(HttpStatus.OK).body(sales);
    }

    @GetMapping("sales/week/profits/{storeId}")
    public ResponseEntity<?> getSalesProfitsPerWeek(@PathVariable UUID storeId, Authentication authentication){
        String username=authentication.getName();
        List<WeeklySalesDto> salesPerList=reportSaleService.groupSalesPerWeek(storeId,username);
        return ResponseEntity.status(HttpStatus.OK).body(salesPerList);
    }
}
