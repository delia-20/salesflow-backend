package com.portafolio.zomtg.salesflow.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.model.entities.Store;
import com.portafolio.zomtg.salesflow.model.entities.User;
import com.portafolio.zomtg.salesflow.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class StoreService {
    StoreRepository storeRepository;
    UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    @Autowired
    public void setStoreRepository(StoreRepository storeRepository, UserRepository userRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
    }

    public Store saveStore(Store store,String username) {
        User owner=userRepository.findUserByUsername(username).orElseThrow(()->new InvalidCredentials("User Not Found"));
        UUID ownerId=owner.getOwnerId();
        store.setOwnerId(ownerId);
        store.setPassword(passwordEncoder.encode(store.getPassword()));
        store=storeRepository.save(store);
        return store;
    }

    public Store getStore(UUID storeId, String username) {
        Store s=storeRepository.findById(storeId).orElseThrow(()-> new ObjectNotFound("Store Not Found"));
        User owner=userRepository.findUserByUsername(username).orElseThrow(()->new ObjectNotFound("User Not Found"));
        if(s.getOwnerId().equals(owner.getOwnerId())){
           return s;
        }
       throw new InvalidCredentials("Inappropriate credentials");

    }
    public boolean deleteStore(UUID storeId, String username) {
        Store store=storeRepository.findById(storeId).orElseThrow(()-> new ObjectNotFound("Store Not Found"));
        User owner=userRepository.findUserByUsername(username).orElseThrow(()->new ObjectNotFound("User Not Found"));

        if(store.getOwnerId().equals(owner.getOwnerId())) {
            storeRepository.delete(store);
            return true;
        }

      throw new InvalidCredentials("Inappropriate credentials");

    }

    public List<Store> getAll(String username) {
        User owner=userRepository.findUserByUsername(username).orElseThrow();

        return storeRepository.findStoreByOwnerId(owner.getOwnerId());
    }
}
