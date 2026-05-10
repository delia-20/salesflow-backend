package com.portafolio.zomtg.salesflow.controller;

import com.portafolio.zomtg.salesflow.model.dto.ClientSaleHistoryDTO;
import com.portafolio.zomtg.salesflow.model.dto.WeeklySalesDto;
import com.portafolio.zomtg.salesflow.model.entities.Sale;
import com.portafolio.zomtg.salesflow.service.DashboardEmployeeService;
import com.portafolio.zomtg.salesflow.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/")
public class EmployeeController {

    private EmployeeService employeeService;
    private DashboardEmployeeService dashboard;
    @Autowired
    public void setEmployeeService(EmployeeService employeeService
    , DashboardEmployeeService dashboard)
    {
        this.employeeService = employeeService;
        this.dashboard = dashboard;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("dashboard/sales/day")
    public ResponseEntity<?> salesPerDay(Authentication authentication) {
       List<WeeklySalesDto> salesPerDay= dashboard.groupSalesPerWeek(authentication.getName());
       return ResponseEntity.ok(salesPerDay);
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("dashboard/sales/week/resume")
    public ResponseEntity<?> resumeSales(Authentication authentication) {
        List<ClientSaleHistoryDTO> sales=dashboard.groupSalesPerWeekResume(authentication.getName());
        return ResponseEntity.ok(sales);
    }

    //administrar productos

}
