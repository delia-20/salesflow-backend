package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.model.dto.SalesRequestDTO;
import com.portafolio.zomtg.salesflow.model.entities.*;
import com.portafolio.zomtg.salesflow.model.enums.Role;
import com.portafolio.zomtg.salesflow.model.enums.StatusSale;
import com.portafolio.zomtg.salesflow.repository.ClientRepository;
import com.portafolio.zomtg.salesflow.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OnlineSaleService {
    ClientRepository clientRepository;
    SaleService saleService;
    SaleRepository saleRepository;
    ReceiptService receiptService;
    @Autowired
    public OnlineSaleService(ClientRepository clientRepository,
                             SaleService saleService,
                             SaleRepository saleRepository,
                             ReceiptService receiptService) {
        this.clientRepository = clientRepository;
        this.saleService = saleService;
        this.saleRepository = saleRepository;
        this.receiptService = receiptService;
    }

    public Sale createSale(SalesRequestDTO request,String usename) {
        Client client=clientRepository.findByUsername(usename).orElseThrow(()->new InvalidCredentials("client not found"));
        Sale sale= saleService.saveSale(request,client.getId());
        return sale;
    }

    public Sale cancelSale(UUID saleId,String usename) {
        Client client=clientRepository.findByUsername(usename).orElseThrow(()->new InvalidCredentials("client not found"));
        Sale sale=saleRepository.findById(saleId).orElseThrow(()->new InvalidCredentials("sale not found"));
        System.out.println("saleId:"+sale.getClientId()+" clienid:"+client.getId());
        if(!client.getId().equals(sale.getClientId())) {
            throw new InvalidCredentials("you can not cancel this sale");
        }
        return saleService.cancelSale(sale);
    }
    public Receipt getTicket(UUID saleId,String usename) {
        Client client=clientRepository.findByUsername(usename).orElseThrow(()->new InvalidCredentials("client not found"));
        Sale sale = saleRepository.findById(saleId).orElseThrow(()->new InvalidCredentials("sale not found"));
        if(!client.getId().equals(sale.getClientId())) {
            throw new InvalidCredentials("you can not get this sale");
        }
        Receipt receipt=receiptService.createTicket(saleId);
        if(receipt!=null){
            sale.setStatus(StatusSale.COMPLETED);
            saleRepository.save(sale);
            return receipt;
        }
        return null;
    }


}
