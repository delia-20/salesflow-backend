package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.model.dto.WeeklySalesDto;
import com.portafolio.zomtg.salesflow.model.entities.Sale;
import com.portafolio.zomtg.salesflow.model.entities.Store;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.repository.SaleRepository;
import com.portafolio.zomtg.salesflow.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.repository.UserRepository;
import jdk.jfr.Timestamp;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ReportSaleService {
     SaleRepository saleRepository;
     UserRepository userRepository;
     StoreRepository storeRepository;
     public ReportSaleService(SaleRepository saleRepository,
                              UserRepository userRepository,
                              StoreRepository storeRepository) {
         this.saleRepository = saleRepository;
         this.userRepository = userRepository;
         this.storeRepository = storeRepository;
     }


     public List<Sale>  salesPerDay(UUID storeId, String username, LocalDate date) {
         System.out.println("salesPerDay");
        User owner =userRepository.findUserByUsername(username).orElseThrow(()-> new InvalidCredentials("you are not allowed"));
        LocalDateTime start=date.atStartOfDay();
        LocalDateTime end=date.atTime(LocalTime.MAX);
        List<Sale> salesPerDay=saleRepository.findSalesByStoreAndDate(storeId,start,end);
        return salesPerDay;

     }
    public List<Sale> getSalesByWeek(String username,UUID storeId, LocalDate date) {
         User owner = userRepository.findUserByUsername(username).orElseThrow(()-> new InvalidCredentials("you are not allowed"));
         Store store = storeRepository.findById(storeId).orElseThrow(()-> new ObjectNotFound("store not found"));
         if(!store.getOwnerId().equals(owner.getOwnerId())){
             throw new InvalidCredentials("you are not allowed");
         }


        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);

        LocalDateTime start = startOfWeek.atStartOfDay();
        LocalDateTime end = endOfWeek.atTime(LocalTime.MAX);

        return saleRepository.findSalesByStoreAndDate(storeId, start, end);
    }
    public List<WeeklySalesDto> groupSalesPerWeek(UUID storeId,String username) {
         User owner=userRepository.findUserByUsername(username).orElseThrow(()-> new InvalidCredentials("you are not allowed"));
         Store store=storeRepository.findById(storeId).orElseThrow(()-> new ObjectNotFound("store not found"));
         if(!store.getOwnerId().equals(owner.getOwnerId())){
             throw new InvalidCredentials("you are not allowed");
         }
        List<Object[]> result=saleRepository.getWeeklySale(storeId);

        List<WeeklySalesDto> weeklySales=new ArrayList<>();
        for(Object[] object:result){
            LocalDateTime date=(LocalDateTime)object[0];
            Double total=((Number)object[1]).doubleValue();
            weeklySales.add(new WeeklySalesDto(date,total));
        }
        return  weeklySales;
        

    }

}
