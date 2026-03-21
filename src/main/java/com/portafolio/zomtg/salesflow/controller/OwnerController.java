package com.portafolio.zomtg.salesflow.controller;

import com.portafolio.zomtg.salesflow.model.dto.UserDTO;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/")
public class OwnerController {
    OwnerService ownerService;
    @Autowired
    public void setOwnerService(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("users/key")
    public ResponseEntity<?> getOwnerId(Authentication authentication) {
        String username = (String) authentication.getName();
        String result=ownerService.getOwnerId(username);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("owner id",result));

    }
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("users/storeKey")
    public ResponseEntity<?> getStoreId(Authentication authentication) {
        String username = (String) authentication.getName();
        String result=ownerService.getStoreId(username);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("store id",result));
    }
    //@GetMapping("employees")
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("owner/employees/store/{storeId}")
    public ResponseEntity<List<User>> getAllEmployeeByStore(@PathVariable UUID storeId, Authentication authentication) {
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        String username=userDetails.getUsername();

        List<User> employees= ownerService.getEmployeesByStoreId(username,storeId );

        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }
    @PreAuthorize("hasRole('OWNER')")
    @GetMapping("owner/employees")
    public ResponseEntity<List<User>> getAllEmployeeByOwner(Authentication authentication) {
        UserDetails userDetails=(UserDetails) authentication.getPrincipal();
        String username=userDetails.getUsername();

        List<User> employees= ownerService.getEmployeesByOwnerId(username);
        return ResponseEntity.status(HttpStatus.OK).body(employees);
    }


}
