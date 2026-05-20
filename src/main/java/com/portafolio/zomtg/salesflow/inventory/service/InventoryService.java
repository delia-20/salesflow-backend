package com.portafolio.zomtg.salesflow.inventory.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.products.exception.NoEnoughStockException;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.products.exception.ProductNoFound;
import com.portafolio.zomtg.salesflow.inventory.entity.InventoryMovement;
import com.portafolio.zomtg.salesflow.inventory.enums.TypeInventory;
import com.portafolio.zomtg.salesflow.products.entity.Product;
import com.portafolio.zomtg.salesflow.inventory.repository.InventoryRepository;
import com.portafolio.zomtg.salesflow.products.repository.ProductRepository;
import com.portafolio.zomtg.salesflow.store.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.store.entity.Store;
import com.portafolio.zomtg.salesflow.users.repository.UserRepository;
import com.portafolio.zomtg.salesflow.users.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private InventoryRepository repository;
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private StoreRepository storeRepository;
    @Autowired
    public InventoryService(InventoryRepository repository,
                            ProductRepository productRepository,
                            UserRepository userRepository,StoreRepository storeRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    //venta cancelada
    public void cancelledSale(UUID referenceId, UUID productId,int quantity){
        Product product = productRepository.findById(productId).orElseThrow();
        int previousExistence=product.getExistence();
        product.setExistence(product.getExistence()+quantity);
        productRepository.save(product);

        InventoryMovement inventory = new InventoryMovement(
                productId,
                TypeInventory.CANCEL_SALE,
                quantity,
                LocalDateTime.now(),
                referenceId,
                previousExistence,
                product.getExistence()

        );

        repository.save(inventory);


    }


    //venta realizada
    public void createSale(UUID referenceId, UUID productId,int quantity){

        Product product=productRepository.findById(productId).orElseThrow();
        if(product.getExistence()<quantity){
            throw  new NoEnoughStockException("Not enough stock");
        }
        int previousExistence=product.getExistence();
        product.setExistence(product.getExistence()-quantity);
        productRepository.save(product);

        InventoryMovement inventory = new InventoryMovement(
                productId,
                TypeInventory.SALE,
                quantity,
                LocalDateTime.now(),
                referenceId,
                previousExistence,
                product.getExistence()
        );
        repository.save(inventory);
        //return inventory.toString();
    }

    //
    public String addNewProduct(UUID referenceId,UUID productId,int quantity){

        Product product = productRepository.findById(productId).orElseThrow();
        InventoryMovement inventory = new InventoryMovement(
                productId,
                TypeInventory.CREATE_PRODUCT,
                quantity,
                LocalDateTime.now(),
                referenceId,
                0,
                product.getExistence()
        );
        repository.save(inventory);
        return inventory.toString();

    }
    public  String adjustQuantity(UUID referenceId,UUID productId,int quantity,int previusStock,int newStock){
        InventoryMovement inventory = new InventoryMovement();
        inventory.setDateTime(LocalDateTime.now());
        inventory.setReferenceId(referenceId);
        inventory.setProductId(productId);
        inventory.setQuantity(quantity);
        inventory.setType(TypeInventory.ADJUSTMENT);
        inventory.setPreviousStock(previusStock);
        inventory.setNewStock(newStock);
        repository.save(inventory);
        return inventory.toString();
    }

    public String restockQuantity(UUID referenceId,UUID productId,int quantity,int previusStock,int newStock){
        InventoryMovement inventory = new InventoryMovement();
        inventory.setDateTime(LocalDateTime.now());
        inventory.setReferenceId(referenceId);
        inventory.setProductId(productId);
        inventory.setQuantity(quantity);
        inventory.setType(TypeInventory.RESTOCK);
        inventory.setPreviousStock(previusStock);
        inventory.setNewStock(newStock);
        repository.save(inventory);
        return inventory.toString();
    }




    public List<InventoryMovement> getInventoryMovements(String username,UUID productId){
        User owner=userRepository.findUserByUsername(username).orElseThrow(()->new InvalidCredentials("Username not found"));
        Product product=productRepository.findById(productId).orElseThrow(()->new ProductNoFound("Product not found"));
        Store store = storeRepository.findById(product.getStoreId()).orElseThrow(()->new ObjectNotFound("Store not found"));
        if(!store.getOwnerId().equals(owner.getOwnerId())){
            throw  new InvalidCredentials("Username not found");
        }
        List <InventoryMovement> inventoryMovement = repository.findByProductId(productId);
        return  inventoryMovement;


    }


}
