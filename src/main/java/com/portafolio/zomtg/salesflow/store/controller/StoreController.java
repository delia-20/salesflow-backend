package com.portafolio.zomtg.salesflow.store.controller;

import com.portafolio.zomtg.salesflow.store.entity.Store;
import com.portafolio.zomtg.salesflow.store.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class StoreController {
    StoreService storeService;
    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PreAuthorize("hasRole('OWNER')")
    @PostMapping("store")
    public ResponseEntity<?> saveStore(@RequestBody Store store, Authentication authentication) {
        String username = authentication.getName();
        Store store1=storeService.saveStore(store,username);
        return ResponseEntity.ok().body(store1);

    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("store/{storeId}")
    public ResponseEntity<?> getStore(
            @PathVariable UUID storeId,
            Authentication authentication) {

        String username = authentication.getName();

        Store result = storeService.getStore(storeId, username);

        return  ResponseEntity.ok(result);
    }
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("stores")
    public ResponseEntity<?> getAllStores(Authentication authentication) {
        String username = authentication.getName();

        List<Store> result= storeService.getAll(username);
        return ResponseEntity.ok().body(result);

    }

    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("store/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable UUID storeId,Authentication authentication) {
        String username = authentication.getName();
        storeService.deleteStore(storeId,username);
        return ResponseEntity.ok().build();

    }
}
