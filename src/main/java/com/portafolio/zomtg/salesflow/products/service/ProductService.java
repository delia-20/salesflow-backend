package com.portafolio.zomtg.salesflow.products.service;

import com.portafolio.zomtg.salesflow.exception.BusinessOperationException;
import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.products.dto.ProductDTO;
import com.portafolio.zomtg.salesflow.products.dto.ProductRequest;
import com.portafolio.zomtg.salesflow.products.dto.ProductResponse;
import com.portafolio.zomtg.salesflow.products.entity.Product;
import com.portafolio.zomtg.salesflow.inventory.service.InventoryService;
import com.portafolio.zomtg.salesflow.products.mapper.ProductMapper;
import com.portafolio.zomtg.salesflow.store.entity.Store;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.products.repository.ProductRepository;
import com.portafolio.zomtg.salesflow.store.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.users.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private ProductRepository productRepository;
    private UserRepository userRepository;
    private StoreRepository storeRepository;
    InventoryService inventoryService;

    ProductMapper  productMapper;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          UserRepository userRepository,
                          StoreRepository storeRepository,
                          InventoryService inventoryService,ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
        this.inventoryService = inventoryService;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductResponse save (ProductRequest request, String username) {
        Product product = productMapper.toProduct(request);
        System.out.println("storeid " + product.getStoreId());
        User owner = userRepository.findUserByUsername(username).orElseThrow();
        Store store = storeRepository.findById(product.getStoreId()).orElseThrow();
        int quantity=product.getExistence();


        if (!store.getOwnerId().equals(owner.getOwnerId())) {
            throw new InvalidCredentials("Invalid credentials");
        }
        try {

            store.setNumProducts(store.getNumProducts() + 1);//
            store.setNumTotalItems(store.getNumTotalItems() + product.getExistence());
            storeRepository.save(store);
            product=productRepository.save(product);
            inventoryService.addNewProduct(owner.getOwnerId(), product.getId(), quantity);

            return productMapper.toProductResponse(product);
        }catch (ObjectOptimisticLockingFailureException e) {
            throw new BusinessOperationException("Not enough products");
        }
    }


    public Product updateProduct(UUID productId, ProductDTO request, String username) {
        User owner = userRepository.findUserByUsername(username).orElseThrow();
        Product updated=productRepository.findById(productId).orElseThrow();
        Store store=storeRepository.findById(updated.getStoreId()).orElseThrow();
        int previousStock=updated.getExistence();


        if(!store.getOwnerId().equals(owner.getOwnerId())){
            throw new InvalidCredentials("Invalid credentials");
        }

        if (request.getExistence() > 0) {
            updated.setExistence(request.getExistence() + updated.getExistence());
            store.setNumTotalItems(store.getNumTotalItems() + request.getExistence());
            storeRepository.save(store);
        }
        if (request.getName() != null)
            updated.setName(request.getName());
        if (request.getPrice() != null)
            updated.setPrice(request.getPrice());
        if (request.getDescription() != null)
            updated.setDescription(request.getDescription());
        if (request.getCategory() != null)
            updated.setCategory(request.getCategory());


        productRepository.save(updated);
        String movement = inventoryService.restockQuantity(owner.getOwnerId(), productId, request.getExistence(), previousStock, updated.getExistence());


        System.out.println(movement);
        return updated;

    }

    public List<Product> getAll() {

        List<Product> products=productRepository.findAll();
        return products;
    }


    public List<Product> getByStore(String username, UUID storeId) {
        User owner = userRepository.findUserByUsername(username).orElseThrow();
        Store store=storeRepository.findById(storeId).orElseThrow();
        if(store.getOwnerId().equals(owner.getOwnerId())){
            List<Product> products=productRepository.findByStoreId(storeId);
            return products;
        }
        throw new InvalidCredentials("Invalid credentials");

    }

    public boolean deleteProduct(UUID productId, String username) {
        User owner =userRepository.findUserByUsername(username).orElseThrow();
        Product product =productRepository.findById(productId).orElseThrow();
        Store store=storeRepository.findById(product.getStoreId()).orElseThrow();
        if(store.getOwnerId().equals(owner.getOwnerId())){
            productRepository.delete(product);
            return true;
        }
        throw  new InvalidCredentials("Invalid credentials");
    }

    public Product deactivateProduct(UUID productId, String username) {
        User owner =userRepository.findUserByUsername(username).orElseThrow();
        Product product =productRepository.findById(productId).orElseThrow();
        Store store=storeRepository.findById(product.getStoreId()).orElseThrow();
        if(store.getOwnerId().equals(owner.getOwnerId())){
            product.setActive(false);
            return  productRepository.save(product);
        }
        throw  new InvalidCredentials("Invalid credentials, you can't deactivate this product");
    }

    public Product activateProduct(UUID productId, String username) {
        User owner =userRepository.findUserByUsername(username).orElseThrow();
        Product product =productRepository.findById(productId).orElseThrow();
        Store store=storeRepository.findById(product.getStoreId()).orElseThrow();
        if(store.getOwnerId().equals(owner.getOwnerId())){
            product.setActive(true);
            return  productRepository.save(product);
        }
        throw  new InvalidCredentials("Invalid credentials, you can't activate this product");
    }
}
