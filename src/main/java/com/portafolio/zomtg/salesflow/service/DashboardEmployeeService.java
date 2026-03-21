package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.model.dto.ClientSaleHistoryDTO;
import com.portafolio.zomtg.salesflow.model.dto.SalesItemsRequestDTO;
import com.portafolio.zomtg.salesflow.model.dto.WeeklySalesDto;
import com.portafolio.zomtg.salesflow.model.entities.*;
import com.portafolio.zomtg.salesflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DashboardEmployeeService {
    UserRepository userRepository;
    StoreRepository storeRepository;
    SaleRepository saleRepository;
    SaleItemRepository saleItemRepository;
    ProductRepository productRepository;

    @Autowired
    public DashboardEmployeeService(UserRepository userRepository,
                                    StoreRepository storeRepository,
                                    SaleRepository saleRepository,
                                    SaleItemRepository saleItemRepository,
                                    ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.saleRepository = saleRepository;
        this.saleItemRepository = saleItemRepository;
        this.productRepository = productRepository;

    }



    public List<WeeklySalesDto> groupSalesPerWeek(String username) {
        User employee=userRepository.findUserByUsername(username).orElseThrow(()-> new InvalidCredentials("you are not allowed"));

        Store store=storeRepository.findById(employee.getStoreId()).orElseThrow(()-> new ObjectNotFound("store not found"));
        if(!store.getOwnerId().equals(employee.getOwnerId())){
            throw new InvalidCredentials("you are not allowed");
        }
        List<Object[]> result=saleRepository.getWeeklySaleByEmployee(employee.getStoreId(),employee.getId());

        List<WeeklySalesDto> weeklySales=new ArrayList<>();
        for(Object[] object:result){
            LocalDateTime date=(LocalDateTime)object[0];
            Double total=((Number)object[1]).doubleValue();
            weeklySales.add(new WeeklySalesDto(date,total));
        }
        return  weeklySales;


    }

    public List<Sale>  salesPerDay(String username, LocalDate date) {

        User employee =userRepository.findUserByUsername(username).orElseThrow(()-> new InvalidCredentials("you are not allowed"));
        LocalDateTime start=date.atStartOfDay();
        LocalDateTime end=date.atTime(LocalTime.MAX);
        List<Sale> salesPerDay=saleRepository.findSalesByStoreAndDate(employee.getStoreId(),start,end);
        return salesPerDay;

    }

    public List<ClientSaleHistoryDTO> groupSalesPerWeekResume(String name) {
        User employee = userRepository.findUserByUsername(name).orElseThrow(()-> new InvalidCredentials("you are not allowed"));
        List<Sale> sales=saleRepository.findByEmployeeIdAndStoreId(employee.getId(),employee.getStoreId());
        List<ClientSaleHistoryDTO> result=new ArrayList<>();
        for(Sale sale:sales){
            List<SaleItem> saleItems=saleItemRepository.findBySaleId(sale.getId());
            List<SalesItemsRequestDTO> items=new ArrayList<>();
            for(SaleItem saleItem:saleItems){
                Product p=productRepository.findById(saleItem.getProductId()).orElseThrow(()-> new ObjectNotFound("product not found"));
                SalesItemsRequestDTO item=new SalesItemsRequestDTO(
                        saleItem.getProductId(),
                        saleItem.getQuantity(),
                        saleItem.getPriceAtMoment(),
                        saleItem.getSubtotal(),
                        p.getName()
                );
                items.add(item);
            }
            result.add(new ClientSaleHistoryDTO(
                    sale.getSaleNumber(),
                    sale.getTotal(),
                    sale.getDatetime(),
                    items
            ));

        }
        return result;

    }

}
