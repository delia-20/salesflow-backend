package com.portafolio.zomtg.salesflow.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public")
public class HomeController {
    @GetMapping("/health")
    public String home() {
        return " Sales Management System API is running \\n!-------------------version 3 revisar pipeline let's go \\n todo debe funcionar ahora";
    }

    @GetMapping("/home")
    public String home1() {
        return "Sales Management System API is running!";
    }
}

