package com.portafolio.zomtg.salesflow;

import com.portafolio.zomtg.salesflow.auth.service.AuthorizationService;
import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.UnauthorizedOperationException;
import com.portafolio.zomtg.salesflow.products.entity.Product;
import com.portafolio.zomtg.salesflow.products.repository.ProductRepository;
import com.portafolio.zomtg.salesflow.sales.dto.SaleResponse;
import com.portafolio.zomtg.salesflow.sales.dto.SalesItemsRequestDTO;
import com.portafolio.zomtg.salesflow.sales.dto.SalesRequestDTO;
import com.portafolio.zomtg.salesflow.sales.mapper.SaleMapper;
import com.portafolio.zomtg.salesflow.users.enums.Role;
import com.portafolio.zomtg.salesflow.sales.enums.StatusSale;
import com.portafolio.zomtg.salesflow.sales.entity.Sale;
import com.portafolio.zomtg.salesflow.sales.entity.SaleItem;
import com.portafolio.zomtg.salesflow.sales.repository.SaleItemRepository;
import com.portafolio.zomtg.salesflow.sales.repository.SaleRepository;
import com.portafolio.zomtg.salesflow.inventory.service.InventoryService;
import com.portafolio.zomtg.salesflow.receipt.service.ReceiptService;
import com.portafolio.zomtg.salesflow.sales.service.SaleService;
import com.portafolio.zomtg.salesflow.store.entity.Store;
import com.portafolio.zomtg.salesflow.store.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {
    @Mock
    SaleRepository saleRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    StoreRepository storeRepository;
    @Mock
    ProductRepository productRepository;
    @Mock
    SaleItemRepository saleItemRepository;
    @Mock
    ReceiptService receiptService;
    @Mock
    InventoryService inventoryService;
    @Mock
    AuthorizationService  authorizationService;
    @Mock
    SaleMapper saleMapper;


    @Spy
    @InjectMocks
    SaleService saleService;
    @Mock
    User user;
    @Mock
    Store store;
    @Mock
    Sale sale;



    String username;
    UUID storeId;
    UUID ownerId;
    UUID productId;
    UUID wrongOwnerId;
    UUID userId;
    UUID saleId;

    @BeforeEach
    public void setup() {
        username = "admin";
        storeId = UUID.fromString("6694c81b-a427-4447-858b-1beda8c1d80a");
        ownerId=UUID.fromString("17ac43b7-16eb-4bb6-afc5-f7e2d56d3b4c");
        productId = UUID.fromString("3b1485cf-fa17-4e74-9c78-267c8a4f6647");
        userId=UUID.fromString("01129ab6-7910-4a64-b160-e0941b8c005f");
        saleId= UUID.fromString("4ac7507a-8e89-4c4a-bb80-13fe3e5bda1d");


    }

    @Test
    public  void shouldCreateSale_whenUserIsOwner(){
        Product product = new Product(
                "product",
                " ",
                5.00,
                "image",
                "category",
                3,
                storeId
        );
        product.setId(productId);
        user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRole(Role.OWNER);
        user.setOwnerId(ownerId);

        store = new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);

        SalesItemsRequestDTO saleItem = new SalesItemsRequestDTO();
        saleItem.setProductId(productId);
        saleItem.setQuantity(1);

        List<SalesItemsRequestDTO> saleItems = new ArrayList<>();
        saleItems.add(saleItem);
        SalesRequestDTO salesRequestDTO = new SalesRequestDTO();
        salesRequestDTO.setStoreId(storeId);
        salesRequestDTO.setItems(saleItems);

        when(authorizationService.validateStoreAccess(username,storeId)).thenReturn(user);
        when(saleMapper.toSaleResponse(any(Sale.class)))
                .thenAnswer(invocation -> {

                    Sale s = invocation.getArgument(0);

                    return new SaleResponse(
                            s.getId(),
                            s.getStoreId(),
                            s.getEmployeeId(),
                            s.getDatetime(),
                            s.getTotal(),
                            s.getStatus(),
                            s.getPaymentMethod(),
                            s.getClientId(),
                            s.getSaleNumber()
                    );
                });
        when(saleRepository.save(any(Sale.class)))
                .thenAnswer(invocation -> {
                    Sale sale = invocation.getArgument(0);
                    if(sale.getId() == null){
                        sale.setId(saleId);
                    }

                    return sale;
                });
        when(saleItemRepository.save(any(SaleItem.class))).thenReturn(new SaleItem());
        doReturn("saleNumber")
                .when(saleService)
                .generateSaleNumber();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        SaleResponse sale=saleService.createSale(salesRequestDTO,username);


        assertEquals(storeId, sale.storeId());
        assertEquals(userId, sale.employeeId());
        assertEquals(StatusSale.IN_PROGRESS,sale.status());
        assertEquals("saleNumber", sale.saleNumber());
        assertEquals(5.0, sale.total());

        verify(inventoryService).createSale(saleId,productId,1);
        verify(saleItemRepository).save(any(SaleItem.class));
    }

    @Test
    public  void shouldCreateSale_whenEmployeeBelongsToStore(){
        Product product = new Product(
                "product",
                " ",
                5.00,
                "image",
                "category",
                3,
                storeId
        );
        product.setId(productId);
        user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRole(Role.EMPLOYEE);
        user.setStoreId(storeId);
        user.setOwnerId(ownerId);

        store = new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);

        SalesItemsRequestDTO saleItem = new SalesItemsRequestDTO();
        saleItem.setProductId(productId);
        saleItem.setQuantity(1);

        List<SalesItemsRequestDTO> saleItems = new ArrayList<>();
        saleItems.add(saleItem);
        SalesRequestDTO salesRequestDTO = new SalesRequestDTO();
        salesRequestDTO.setStoreId(storeId);
        salesRequestDTO.setItems(saleItems);


        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(saleItemRepository.save(any(SaleItem.class))).thenReturn(new SaleItem());
        doReturn("saleNumber").when(saleService).generateSaleNumber();
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(authorizationService.validateStoreAccess(username,storeId)).thenReturn(user);
        when(saleMapper.toSaleResponse(any(Sale.class)))
                .thenAnswer(invocation -> {

                    Sale s = invocation.getArgument(0);

                    return new SaleResponse(
                            s.getId(),
                            s.getStoreId(),
                            s.getEmployeeId(),
                            s.getDatetime(),
                            s.getTotal(),
                            s.getStatus(),
                            s.getPaymentMethod(),
                            s.getClientId(),
                            s.getSaleNumber()
                    );
                });

        SaleResponse sale=saleService.createSale(salesRequestDTO,username);

        ArgumentCaptor<Sale> captor = ArgumentCaptor.forClass(Sale.class);
        verify(saleRepository, times(2)).save(captor.capture());

        List<Sale> sales = captor.getAllValues();
        Sale sale1=sales.get(0);
        Sale sale2=sales.get(1);
        assertEquals(storeId, sale2.getStoreId());
        assertEquals(userId, sale2.getEmployeeId());
        assertEquals(StatusSale.IN_PROGRESS, sale2.getStatus());
        assertEquals("saleNumber", sale2.getSaleNumber());
        assertEquals(5.0, sale2.getTotal());

        verify(inventoryService).createSale(sale.id(),productId,1);
        verify(saleItemRepository).save(any(SaleItem.class));
    }



    @Test
    public void shouldThrowException_whenUserNotAuthorized(){
        Product product = new Product(
                "product",
                " ",
                5.00,
                "image",
                "category",
                3,
                storeId
        );
        product.setId(productId);

        user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRole(Role.OWNER);
        wrongOwnerId=UUID.randomUUID();
        user.setOwnerId(wrongOwnerId);

        store = new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);

        SalesItemsRequestDTO saleItem = new SalesItemsRequestDTO();
        saleItem.setProductId(productId);
        saleItem.setQuantity(1);

        List<SalesItemsRequestDTO> saleItems = new ArrayList<>();
        saleItems.add(saleItem);
        SalesRequestDTO salesRequestDTO = new SalesRequestDTO();
        salesRequestDTO.setStoreId(storeId);
        salesRequestDTO.setItems(saleItems);
        when(authorizationService.validateStoreAccess(username,storeId)).thenThrow(new UnauthorizedOperationException("Denied access"));


        assertThrows(UnauthorizedOperationException.class, ()->saleService.createSale(salesRequestDTO,username) ) ;
        verify(saleRepository,never()).save(any(Sale.class));


    }
    @Test
    public void shouldCancelSaleAndRestoreInventory(){
        Product product = new Product(
                "product",
                " ",
                5.00,
                "image",
                "category",
                3,
                storeId
        );
        product.setId(productId);

        user = new User();
        user.setId(userId);
        user.setUsername(username);
        user.setRole(Role.OWNER);
        user.setOwnerId(ownerId);
        store = new Store();
        store.setId(storeId);
        store.setOwnerId(ownerId);

        UUID saleId = UUID.randomUUID();
        sale=new Sale();
        sale.setId(saleId);
        sale.setStoreId(storeId);

        SaleItem item = new SaleItem();
        item.setProductId(productId);
        item.setQuantity(1);
        List<SaleItem> saleItems = new ArrayList<>();
        saleItems.add(item);

        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(saleItemRepository.findBySaleId(saleId)).thenReturn(saleItems);
        when(saleRepository.save(any(Sale.class))).thenAnswer(invocation -> invocation.getArgument(0));


        Sale sale=saleService.cancelledSale(saleId,username);
        verify(saleRepository,times(1)).save(any(Sale.class));
        assertEquals(saleId,sale.getId());
        assertEquals(StatusSale.CANCELLED,sale.getStatus());

    }


}
