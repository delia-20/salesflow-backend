package com.portafolio.zomtg.salesflow.receipt.controller;

import com.portafolio.zomtg.salesflow.receipt.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/")
public class ReceiptController {


    ReceiptService receiptService;
    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;

    }

}
