package com.portafolio.zomtg.salesflow.receipt.service;

import com.portafolio.zomtg.salesflow.receipt.entity.Receipt;
import com.portafolio.zomtg.salesflow.sales.entity.Sale;
import com.portafolio.zomtg.salesflow.receipt.repository.ReceiptRepository;
import com.portafolio.zomtg.salesflow.sales.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReceiptService {

    ReceiptRepository repository;
    SaleRepository saleRepository;
    @Autowired
    public ReceiptService(ReceiptRepository repository,SaleRepository saleRepository) {
        this.repository = repository;
        this.saleRepository = saleRepository;
    }

    public Receipt createTicket(UUID saleId) {
        Receipt newReceipt = new Receipt();
        Sale sale = saleRepository.findById(saleId).orElse(null);

        newReceipt.setDateTime(LocalDateTime.now());
        newReceipt.setSaleId(sale.getId());
        newReceipt.setTotal(sale.getTotal());
        newReceipt.setFolio(UUID.randomUUID());
        Double total = sale.getTotal();
        Double tax= total*0.1;
        newReceipt.setTax(tax);
        if(sale.getClientId() != null)
            newReceipt.setClientId(sale.getClientId());
        return repository.save(newReceipt);
    }

}
