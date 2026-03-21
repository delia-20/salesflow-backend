package com.portafolio.zomtg.salesflow;

import com.portafolio.zomtg.salesflow.exception.NoEnoughStockException;
import com.portafolio.zomtg.salesflow.model.entities.InventoryMovement;
import com.portafolio.zomtg.salesflow.model.entities.Product;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.model.enums.TypeInventory;
import com.portafolio.zomtg.salesflow.repository.InventoryRepository;
import com.portafolio.zomtg.salesflow.repository.ProductRepository;
import com.portafolio.zomtg.salesflow.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.repository.UserRepository;
import com.portafolio.zomtg.salesflow.service.InventoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    ProductRepository productRepository;

    @Mock
    InventoryRepository inventoryRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    StoreRepository storeRepository;

    @InjectMocks
    InventoryService inventoryService;

    UUID productId;
    UUID storeId;
    UUID referenceId;

    @BeforeEach
    void setUp() {
        storeId = UUID.fromString("6694c81b-a427-4447-858b-1beda8c1d80a");
        productId = UUID.fromString("3b1485cf-fa17-4e74-9c78-267c8a4f6647");
        referenceId = UUID.fromString("e23486ca-70b1-4fcb-804b-ab16e35cdd23");
    }


    @Test
    public void shouldReduceStockAndSaveMovement_whenSaleIsCreated() {

        Product realProduct = new Product(
                "Liebre",
                " ",
                5.00,
                "image",
                "category",
                3,
                true,
                storeId
        );

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(realProduct));

        when(productRepository.save(any(Product.class)))
                .thenReturn(realProduct);

        when(inventoryRepository.save(any(InventoryMovement.class)))
                .thenReturn(new InventoryMovement());

        inventoryService.createSale(referenceId, productId, 2);

        assertEquals(1, realProduct.getExistence());

        verify(productRepository).save(realProduct);

        ArgumentCaptor<InventoryMovement> captor =
                ArgumentCaptor.forClass(InventoryMovement.class);

        verify(inventoryRepository).save(captor.capture());

        InventoryMovement movement = captor.getValue();

        assertEquals(TypeInventory.SALE, movement.getType());
        assertEquals(2, movement.getQuantity());
        assertEquals(3, movement.getPreviousStock());
        assertEquals(1, movement.getNewStock());
    }

    @Test
    public void shouldThrowException_whenNotEnoughStock(){
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
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        assertThrows(NoEnoughStockException.class, () -> inventoryService.createSale(referenceId, productId, 5));
        verify(productRepository, never()).save(any());
        verify(inventoryRepository, never()).save(any());


    }
    @Test
    public void shouldRestoreStock_whenSaleIsCancelled(){
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
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(inventoryRepository.save(any(InventoryMovement.class))).thenReturn(new InventoryMovement());

        inventoryService.cancelledSale(referenceId, productId, 2);
        assertEquals(5, product.getExistence());
        verify(productRepository).save(product);
        ArgumentCaptor<InventoryMovement> captor =
                ArgumentCaptor.forClass(InventoryMovement.class);
        verify(inventoryRepository).save(captor.capture());
        InventoryMovement movement = captor.getValue();
        assertEquals(TypeInventory.CANCEL_SALE, movement.getType());
        assertEquals(2, movement.getQuantity());
        assertEquals(3, movement.getPreviousStock());
        assertEquals(5, movement.getNewStock());

    }

    public void shouldReturnMovements_whenUserOwnsStore(){


    }

}
