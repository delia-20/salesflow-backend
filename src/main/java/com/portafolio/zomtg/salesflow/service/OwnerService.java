package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.model.entities.Store;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.model.enums.Role;
import com.portafolio.zomtg.salesflow.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class OwnerService {
    UserRepository userRepository;
    StoreRepository storeRepository;
    @Autowired
    public OwnerService(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public String getOwnerId(String username) {
        User owner =userRepository.findUserByUsername(username).orElseThrow(() ->new InvalidCredentials("Username not found"));

        return owner.getOwnerId().toString();

    }
    public String getStoreId(String username) {
        User owner =userRepository.findUserByUsername(username).orElseThrow(() ->new InvalidCredentials("Username not found"));
        return owner.getStoreId().toString();

    }
    public List<User> getEmployeesByStoreId(String username,UUID storeId) {
        User owner = userRepository.findUserByUsername(username).orElseThrow();
        Store store = storeRepository.findStoreById(storeId).orElseThrow();
        if (store.getOwnerId().equals(owner.getOwnerId())) {
            List<User> employees=userRepository.findByStoreIdAndRole(storeId, Role.EMPLOYEE);
            return employees;
        }

        throw  new InvalidCredentials("actions no allowed");
    }

    //
    public List<User> getEmployeesByOwnerId(String username) {
        User owner = userRepository.findUserByUsername(username).orElseThrow(()->new InvalidCredentials("Username not found"));
        UUID ownerId = owner.getOwnerId();
        List<User> employees=userRepository.findByOwnerIdAndRole(ownerId,Role.EMPLOYEE);
        return employees;
    }
    //

}
