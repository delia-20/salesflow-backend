package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.model.dto.ClientSaleHistoryDTO;
import com.portafolio.zomtg.salesflow.model.dto.SalesItemsRequestDTO;
import com.portafolio.zomtg.salesflow.model.entities.Client;
import com.portafolio.zomtg.salesflow.model.entities.Product;
import com.portafolio.zomtg.salesflow.model.entities.Sale;
import com.portafolio.zomtg.salesflow.model.entities.SaleItem;
import com.portafolio.zomtg.salesflow.repository.ClientRepository;
import com.portafolio.zomtg.salesflow.repository.ProductRepository;
import com.portafolio.zomtg.salesflow.repository.SaleItemRepository;
import com.portafolio.zomtg.salesflow.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DashboardClientService {

    SaleRepository saleRepository;
    ClientRepository clientRepository;
    SaleItemRepository saleItemRepository;
    ProductRepository productRepository;

    @Autowired
    public DashboardClientService(SaleRepository saleRepository,
                                  ClientRepository clientRepository,
                                  SaleItemRepository saleItemRepository,
                                  ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.clientRepository = clientRepository;
        this.saleItemRepository = saleItemRepository;
        this.productRepository = productRepository;
    }

    //sales por cliente
    public List<Sale> clientSalesHistory(String username){
        Client client = clientRepository.findByUsername(username).orElseThrow(()->  new InvalidCredentials("You are not allowed"));
        List<Sale> sales= saleRepository.findByClientId(client.getId());
        return sales;

    }

    public List<ClientSaleHistoryDTO> getClientSalesHistory(String username){
        Client client=clientRepository.findByUsername(username).orElseThrow(()->  new InvalidCredentials("You are not allowed"));
        List<Sale> sales=saleRepository.findByClientId(client.getId());
        List<ClientSaleHistoryDTO> result=new ArrayList<>();
        for(Sale sale:sales){
            List<SaleItem> saleItems=saleItemRepository.findBySaleId(sale.getId());
            List<SalesItemsRequestDTO> items=new ArrayList<>();
            for(SaleItem saleI:saleItems){
                Product p=productRepository.findById(saleI.getProductId()).orElseThrow(()->new InvalidCredentials("Product not found"));
                SalesItemsRequestDTO item=new SalesItemsRequestDTO(
                        saleI.getProductId(),
                        saleI.getQuantity(),
                        saleI.getPriceAtMoment(),
                        saleI.getSubtotal(),
                        p.getName()
                );
                items.add(item);
            }
            ClientSaleHistoryDTO dto= new ClientSaleHistoryDTO(
                    sale.getSaleNumber(),
                    sale.getTotal(),
                    sale.getDatetime(),
                    items
            );
            result.add(dto);
        }
        System.out.println("result + " +result.size());
        return result;

    }
//    public Map<Sale,Object[]> getClientSalesHistory(String username){
//        Client client=clientRepository.findByUsername(username).orElseThrow(()->  new InvalidCredentials("You are not allowed"));
//        List<Sale> sales=saleRepository.findByClientId(client.getId());
//        Map<String,Object[]> map=new HashMap<>();
//
//        for(Sale sale:sales){
//            String saleData=sale.toString();
//            List<SalesItemsRequestDTO> items=new ArrayList<>();
//            List<SaleItem> saleItem=saleItemRepository.findBySaleId(sale.getId());
//            for(SaleItem saleI:saleItem){
//                Product p=productRepository.findById(saleI.getProductId()).get();
//                SalesItemsRequestDTO item = new SalesItemsRequestDTO(
//                        saleI.getProductId(),
//                        saleI.getQuantity(),
//                        saleI.getPriceAtMoment(),
//                        saleI.getSubtotal(),
//                        p.getName()
//                        );
//                items.add(item);
//            }
//            map.put(saleData,items.toArray());
//
//
//        }
//        return map;
//
//    }




}
