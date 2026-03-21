package com.portafolio.zomtg.salesflow.controller;

import com.portafolio.zomtg.salesflow.model.dto.ProductDTO;
import com.portafolio.zomtg.salesflow.model.entities.Product;
import com.portafolio.zomtg.salesflow.repository.ProductRepository;
import com.portafolio.zomtg.salesflow.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class ProductController {

    private ProductService productService;
    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("products")
    public ResponseEntity<?>  save(@RequestBody Product product, Authentication authentication) {
        String username = authentication.getName();
       Product result=productService.save(product,username);
       return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    //pruebas
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products=productService.getAll();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PreAuthorize("hasRole('OWNER') or hasRole('EMPLOYEE')")
    @PutMapping("products/{productId}")
    public ResponseEntity<?>updateProduct(@PathVariable("productId") UUID productId, @RequestBody ProductDTO request, Authentication authentication) {
        String username = authentication.getName();

        Product result=productService.updateProduct(productId,request,username);
        return ResponseEntity.status(HttpStatus.OK).body(result);



    }

    @PreAuthorize("hasRole('OWNER') or hasRole('EMPLOYEE')")
    @DeleteMapping("product/{productId}")
    public ResponseEntity<?>deleteProduct(@PathVariable("productId") UUID productId, Authentication authentication) {
        String username = authentication.getName();
        boolean result= productService.deleteProduct(productId,username);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("products/{productId}/deactivate")
    public ResponseEntity<?>deactivateProduct(@PathVariable("productId") UUID productId, Authentication authentication) {
        String username = authentication.getName();
        Product result=productService.deactivateProduct(productId,username);
            return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("products/{productId}/activate")
    public ResponseEntity<?>activateProduct(@PathVariable("productId") UUID productId, Authentication authentication) {
        String username = authentication.getName();
        Product result=productService.activateProduct(productId,username);
            return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("products/{storeId}")
    public ResponseEntity<?> getProductByStore(@PathVariable("storeId") UUID storeId, Authentication authentication) {
        String username = authentication.getName();
        List<Product> result=productService.getByStore(username,storeId);
        return ResponseEntity.status(HttpStatus.OK).body(result);


    }
}
