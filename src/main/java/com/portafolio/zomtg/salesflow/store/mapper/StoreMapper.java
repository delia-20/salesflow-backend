package com.portafolio.zomtg.salesflow.store.mapper;

import com.portafolio.zomtg.salesflow.store.dto.StoreRequest;
import com.portafolio.zomtg.salesflow.store.dto.StoreResponse;
import com.portafolio.zomtg.salesflow.store.entity.Store;
import org.springframework.stereotype.Component;

@Component
public class StoreMapper {
    public Store toStore(StoreRequest request)
    {
        Store store = new Store();
        store.setName(request.name());
        store.setDescription(request.description());
        store.setCategory(request.category());
        store.setAddress(request.address());
        store.setPhone(request.phone());
        store.setEmail(request.email());
        store.setPassword(request.password());
        store.setWebsite(request.website());
        store.setOwnerId(request.ownerId());
        return store;

    }

    public StoreResponse toStoreResponse(Store store){
        StoreResponse response = new StoreResponse(
                store.getId(),
                store.getName(),
                store.getDescription(),
                store.getCategory(),
                store.getAddress(),
                store.getPhone(),
                store.getEmail(),
                store.getNumEmployees(),
                store.getNumProducts(),
                store.getNumTotalItems(),
                store.getWebsite(),
                store.getOwnerId()
        );
        return response;
    }
}
