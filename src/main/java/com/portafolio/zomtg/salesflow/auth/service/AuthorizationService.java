package com.portafolio.zomtg.salesflow.auth.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.exception.UnauthorizedOperationException;
import com.portafolio.zomtg.salesflow.store.entity.Store;
import com.portafolio.zomtg.salesflow.store.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthorizationService {

    UserRepository userRepository;
    StoreRepository storeRepository;
    public AuthorizationService(UserRepository userRepository,
                                StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public User validateStoreAccess(String username, UUID storeId) {

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(()-> new InvalidCredentials("No allowed actions"));

        Store store= storeRepository.findStoreById(storeId).orElseThrow(
                ()-> new ObjectNotFound("Store not found")
        );
        if(!store.getOwnerId().equals(user.getOwnerId())){
            throw  new UnauthorizedOperationException("Access denied");
        }
        return user;
    }
}
