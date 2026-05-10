package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.exception.ProductNoFound;
import com.portafolio.zomtg.salesflow.exception.SystemInterErrorException;
import com.portafolio.zomtg.salesflow.model.dto.SalesItemsRequestDTO;
import com.portafolio.zomtg.salesflow.model.dto.SalesRequestDTO;
import com.portafolio.zomtg.salesflow.model.entities.*;
import com.portafolio.zomtg.salesflow.model.enums.PaymentMethod;
import com.portafolio.zomtg.salesflow.model.enums.Role;
import com.portafolio.zomtg.salesflow.model.enums.StatusSale;
import com.portafolio.zomtg.salesflow.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class SaleService {
    @PersistenceContext
            private EntityManager entityManager;

    SaleRepository saleRepository;
    UserRepository userRepository;
    StoreRepository storeRepository;
    ProductRepository productRepository;
    SaleItemRepository saleItemRepository;
    ReceiptService receiptService;
    InventoryService inventoryService;

    @Autowired
    public SaleService(SaleRepository saleRepository,
                       UserRepository userRepository,
                       StoreRepository storeRepository,
                       ProductRepository productRepository,
                       SaleItemRepository saleItemRepository,
                       ReceiptService receiptService,
                       InventoryService inventoryService
    ) {
        this.saleRepository = saleRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
        this.saleItemRepository = saleItemRepository;
        this.receiptService = receiptService;
        this.inventoryService = inventoryService;
    }


    @Transactional
    public Sale createSale(SalesRequestDTO request, String username) {
        User owner = userRepository.findUserByUsername(username)
                .orElseThrow(()-> new InvalidCredentials("No allowed actions"));
        UUID userId;
        if(owner.getRole().equals(Role.EMPLOYEE) ){
            if(owner.getStoreId().equals(request.getStoreId())){
                userId=owner.getId();
               return saveSale(request,userId);
            }

        }else if(owner.getRole().equals(Role.OWNER) ){
            Store store=storeRepository.findById(request.getStoreId()).orElseThrow(()-> new ObjectNotFound("Store not found"));
            if(store.getOwnerId().equals(owner.getOwnerId())){
                userId=owner.getId();

               return saveSale(request,userId);

            }
        }
        throw  new InvalidCredentials("No allowed actions");
    }

@Transactional
    public Sale saveSale(SalesRequestDTO request,UUID id) {

        Sale sale = new Sale();
        if(request.getClientId()!=null){
            sale.setClientId(request.getClientId());
        }

        sale.setStoreId(request.getStoreId());
        sale.setEmployeeId(id);
        sale.setDatetime(LocalDateTime.now());
        sale.setStatus(StatusSale.IN_PROGRESS);
        sale.setPaymentMethod(PaymentMethod.PURCHASE);
        try {
            String saleNumber = generateSaleNumber();
            sale.setSaleNumber(saleNumber);
        }catch (Exception e){
            throw  new SystemInterErrorException("Error generating Sale Number");
        }

        saleRepository.save(sale);

        double total=0;
        for(SalesItemsRequestDTO itemDTO: request.getItems()){
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ProductNoFound("product not found"));
            int quantity=itemDTO.getQuantity();
            inventoryService.createSale(sale.getId(),product.getId(),quantity);

            double subtotal=product.getPrice()*quantity;
            total+=subtotal;
            SaleItem saleItem= new SaleItem(
                    sale.getId(),
                    product.getId(),
                    quantity,
                    product.getPrice(),
                    subtotal
            );
            saleItemRepository.save(saleItem);
        }
        sale.setTotal(total);
        sale.setPaymentMethod(request.getPaymentMethod());



        return saleRepository.save(sale);

    }

    public String generateSaleNumber() {
        Long nextVal = (Long) entityManager
                .createNativeQuery("SELECT nextval('sale_number_seq')")
                .getSingleResult();
        LocalDate date=LocalDate.now();
        int year=date.getYear();
        int month=date.getMonthValue();
        return "SALE-"+month+"-"+year+"-:"+nextVal;
    }


    @Transactional
    public Receipt createTicket(UUID saleId, String username) {
        User owner = userRepository.findUserByUsername(username).orElseThrow(()->new ObjectNotFound("User Not Found"));
        Sale sale = saleRepository.findById(saleId).orElseThrow(()->new ObjectNotFound("Sale not found"));
        Store  store = storeRepository.findById(sale.getStoreId()).orElseThrow(()->new ObjectNotFound("Store not found"));
        if(store.getOwnerId().equals(owner.getOwnerId())){
            Receipt receipt=receiptService.createTicket(saleId);
            if(receipt!=null){
                sale.setStatus(StatusSale.COMPLETED);
                saleRepository.save(sale);
                return receipt;
            }
        }
       throw  new InvalidCredentials("No allowed actions");

    }


    public List<Sale> getSales(UUID storeId,String username) {
        User  owner = userRepository.findUserByUsername(username).orElseThrow(()->new InvalidCredentials("No allowed actions"));
        Store store = storeRepository.findById(storeId).orElseThrow(()->new ObjectNotFound("Store not found"));
        if(store.getOwnerId().equals(owner.getOwnerId())) {
            List<Sale> sales = saleRepository.findByStoreId(storeId);
            return sales;
        }
        return null;

    }
    @Transactional
    public Sale cancelledSale(UUID saleId,String username) {
        User owner = userRepository.findUserByUsername(username).orElseThrow();
        Sale sale = saleRepository.findById(saleId).orElseThrow();
        Store store = storeRepository.findById(sale.getStoreId()).orElseThrow();
        if(owner.getRole().equals(Role.OWNER)){
            if(!store.getOwnerId().equals(owner.getOwnerId())){
                throw  new InvalidCredentials("you are not allowed to cancel this sale");
            }
        }else if(owner.getRole().equals(Role.EMPLOYEE)){
            if(!owner.getStoreId().equals(store.getId())){
                throw  new InvalidCredentials("you are not allowed to cancel this sale");
            }
        }

        return cancelSale(sale);
    }
    @Transactional
    public Sale cancelSale(Sale sale) {
        UUID saleId = sale.getId();
        List<SaleItem> items=saleItemRepository.findBySaleId(saleId);
        for(SaleItem saleItem: items){
            Product product = productRepository.findById(saleItem.getProductId()).orElseThrow(()->new ProductNoFound("product not found"));
            inventoryService.cancelledSale(saleId,product.getId(),saleItem.getQuantity());

        }
        sale.setStatus(StatusSale.CANCELLED);
        return  saleRepository.save(sale);
    }

    public Sale deleteSale(UUID saleId, String username) {
        User owner = userRepository.findUserByUsername(username).orElseThrow();
        Sale sale = saleRepository.findById(saleId).orElseThrow();
        Store store = storeRepository.findById(sale.getStoreId()).orElseThrow();
        System.out.println("ownerid:"+owner.getOwnerId() +" storeid:"+store.getOwnerId());
        if(owner.getOwnerId().equals(store.getOwnerId()))
        {
            saleRepository.delete(sale);
            return sale;
        }throw new InvalidCredentials("You are not allowed to delete");

    }

    public List<Sale> getSalesByWeek(UUID storeId, LocalDate date) {

        LocalDate startOfWeek = date.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = date.with(DayOfWeek.SUNDAY);

        LocalDateTime start = startOfWeek.atStartOfDay();
        LocalDateTime end = endOfWeek.atTime(LocalTime.MAX);

        return saleRepository.findSalesByStoreAndDate(storeId, start, end);
    }


}
