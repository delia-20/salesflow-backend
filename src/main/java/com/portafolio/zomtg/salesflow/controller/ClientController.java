package com.portafolio.zomtg.salesflow.controller;

import com.portafolio.zomtg.salesflow.model.dto.ClientSaleHistoryDTO;
import com.portafolio.zomtg.salesflow.model.entities.Client;
import com.portafolio.zomtg.salesflow.model.entities.Sale;
import com.portafolio.zomtg.salesflow.repository.ClientRepository;
import com.portafolio.zomtg.salesflow.service.ClientService;
import com.portafolio.zomtg.salesflow.service.DashboardClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/v1/")
public class ClientController {
    private ClientService clientService;
    private DashboardClientService dashboardClientService;
    @Autowired
    public ClientController(ClientService clientService,
                            DashboardClientService dashboardClientService) {
        this.clientService = clientService;
        this.dashboardClientService = dashboardClientService;
    }

    @GetMapping("clients")
    public ResponseEntity<?>getClients() {
        List<Client> clients=clientService.getClients();
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("result",clients));
    }

    @PostMapping("auth/register/client")
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        Client response= clientService.createClient(client);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("dashboard/sales/history")
    public ResponseEntity<?> getSalesHistory(Authentication authentication) {
        List<Sale> sales=dashboardClientService.clientSalesHistory(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(sales);


    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("dashboard/sales/history/products")
    public ResponseEntity<?> getSalesHistoryProducts(Authentication authentication) {
        List<ClientSaleHistoryDTO> result
                =dashboardClientService.getClientSalesHistory(authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
