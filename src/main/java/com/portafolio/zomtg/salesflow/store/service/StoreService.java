package com.portafolio.zomtg.salesflow.store.service;

import com.portafolio.zomtg.salesflow.exception.InvalidCredentials;
import com.portafolio.zomtg.salesflow.exception.ObjectNotFound;
import com.portafolio.zomtg.salesflow.store.dto.StoreRequest;
import com.portafolio.zomtg.salesflow.store.dto.StoreResponse;
import com.portafolio.zomtg.salesflow.store.entity.Store;
import com.portafolio.zomtg.salesflow.store.mapper.StoreMapper;
import com.portafolio.zomtg.salesflow.users.entity.User;
import com.portafolio.zomtg.salesflow.store.repository.StoreRepository;
import com.portafolio.zomtg.salesflow.users.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class StoreService {
    StoreRepository storeRepository;
    UserRepository userRepository;
    StoreMapper storeMapper;
    private  final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    @Autowired
    public void setStoreRepository(StoreRepository storeRepository, UserRepository userRepository, StoreMapper storeMapper) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.storeMapper = storeMapper;
    }

    public StoreResponse saveStore(StoreRequest request, String username) {
        User owner=userRepository.findUserByUsername(username).orElseThrow(()->new InvalidCredentials("User Not Found"));
        UUID ownerId=owner.getOwnerId();
        Store store=storeMapper.toStore(request);
        store.setOwnerId(ownerId);
        store.setPassword(passwordEncoder.encode(store.getPassword()));
        store=storeRepository.save(store);

        return storeMapper.toStoreResponse(store);
    }

    public StoreResponse getStore(UUID storeId, String username) {
        Store s=storeRepository.findById(storeId).orElseThrow(()-> new ObjectNotFound("Store Not Found"));
        User owner=userRepository.findUserByUsername(username).orElseThrow(()->new ObjectNotFound("User Not Found"));
        if(s.getOwnerId().equals(owner.getOwnerId())){
           return storeMapper.toStoreResponse(s);
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

    public List<StoreResponse> getAll(String username) {
        User owner=userRepository.findUserByUsername(username).orElseThrow();
        List<Store> result= storeRepository.findStoreByOwnerId(owner.getOwnerId());
        List<StoreResponse> responses= new ArrayList<StoreResponse>();
        for(Store store:result){
            responses.add(storeMapper.toStoreResponse(store));
        }
        return responses;
    }
}
