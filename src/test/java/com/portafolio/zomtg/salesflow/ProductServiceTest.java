package com.portafolio.zomtg.salesflow;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.model.dto.ProductDTO;
import com.portafolio.zomtg.salesflow.model.entities.Product;
import com.portafolio.zomtg.salesflow.model.entities.Store;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.model.enums.Role;
import com.portafolio.zomtg.salesflow.repository.ProductRepository;
import com.portafolio.zomtg.salesflow.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.repository.UserRepository;
import com.portafolio.zomtg.salesflow.service.InventoryService;
import com.portafolio.zomtg.salesflow.service.ProductService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    StoreRepository storeRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    User user;
    String username;
    String wrongUsername;
    UUID storeId;
    UUID ownerId;
    UUID productId;
    UUID wrongOwnerId;
    ProductService productService;
    @Mock
    InventoryService inventoryService;
    @Mock
    Store store;
    @Mock
    Product product;


    @BeforeEach
    public void setUp() {
        username = "admin";
        wrongUsername = "bad_username";
        storeId = UUID.fromString("6694c81b-a427-4447-858b-1beda8c1d80a");
        ownerId=UUID.fromString("17ac43b7-16eb-4bb6-afc5-f7e2d56d3b4c");
        productId = UUID.fromString("3b1485cf-fa17-4e74-9c78-267c8a4f6647");
        wrongOwnerId=UUID.fromString("01129ab6-7910-4a64-b160-e0941b8c005f");


        productService= new ProductService(productRepository,userRepository,storeRepository,inventoryService);
    }

    @Test
    public void shouldSaveProductAndUpdateStore_whenValidOwner(){
        Product product = new Product(
                "product",
                " ",
                5.00,
                "image",
                "category",
                3,
                true,
                storeId
        );
        product.setId(productId);
        user=new User();
        user.setOwnerId(ownerId);
        user.setRole(Role.OWNER);
        user.setUsername(username);
        store=new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);
        store.setNumTotalItems(0);
        store.setNumProducts(0);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(inventoryService.addNewProduct(ownerId,productId,product.getExistence())).thenReturn("success");
        Product result=productService.save(product,username);

        verify(userRepository,times(1)).findUserByUsername(username);
        verify(productRepository).save(product);
        verify(storeRepository).save(any(Store.class));
        verify(inventoryService).addNewProduct(ownerId,productId,product.getExistence());
        assertEquals(1,store.getNumProducts());
        assertEquals(3,product.getExistence());
        assertEquals(product,result);




    }

    @Test
    public void shouldSaveProductAndUpdateStore_whenInvalidOwner(){
        Product product = new Product(
                "product",
                " ",
                5.00,
                "image",
                "category",
                3,
                true,
                storeId
        );
        product.setId(productId);
        user=new User();
        user.setOwnerId(wrongOwnerId);
        user.setRole(Role.OWNER);
        user.setUsername(username);
        store=new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);
        store.setNumTotalItems(0);
        store.setNumProducts(0);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));


        assertThrows(InvalidCredentials.class, () -> productService.save(product,username));
        verify(storeRepository,never()).save(any(Store.class));
        verify(productRepository,never()).save(any(Product.class));


    }
    @Test
    public void  shouldUpdateProductStock_whenRequestHasExistence(){

        Product product = new Product(
                "product",
                " ",
                5.00,
                "image",
                "category",
                3,
                true,
                storeId
        );
        product.setId(productId);
        user=new User();
        user.setOwnerId(ownerId);
        user.setRole(Role.OWNER);
        user.setUsername(username);

        store=new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);
        store.setNumTotalItems(0);
        store.setNumProducts(0);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(storeRepository.save(any(Store.class))).thenReturn(store);
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(inventoryService.restockQuantity(any(), any(), anyInt(), anyInt(), anyInt()))
                .thenReturn("success");
        ProductDTO productDTO = new ProductDTO();
        productDTO.setExistence(5);

        Product result=productService.updateProduct(productId,productDTO,username);

        verify(inventoryService).restockQuantity(
                ownerId,
                productId,
                productDTO.getExistence(),
                3,
                result.getExistence()
        );
        assertEquals(8,result.getExistence());

    }

    @Test
    public void shouldReturnProducts_whenUserOwnsStore(){
        Product product = new Product(
                "product",
                " ",
                5.00,
                "image",
                "category",
                3,
                true,
                storeId
        );

        product.setId(productId);

        user=new User();
        user.setOwnerId(ownerId);
        user.setRole(Role.OWNER);
        user.setUsername(username);

        store=new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);

        List<Product> products = new ArrayList<>();
        products.add(product);


        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(productRepository.findByStoreId(storeId)).thenReturn(products);

        List<Product> result=productService.getByStore(username, storeId);

        assertNotNull(result);
        assertEquals(1,result.size());
        assertEquals(productId,result.get(0).getId());

        verify(productRepository).findByStoreId(storeId);

    }

    @Test
    public void shouldReturnNull_whenUserDoesNotOwnStore(){

        user = new User();
        user.setOwnerId(UUID.randomUUID());

        store = new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);

        when(userRepository.findUserByUsername(username))
                .thenReturn(Optional.of(user));

        when(storeRepository.findById(storeId))
                .thenReturn(Optional.of(store));

        List<Product> result = productService.getByStore(username, storeId);

        assertNull(result);

        verify(productRepository, never()).findByStoreId(any());
    }






}
