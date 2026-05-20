package com.portafolio.zomtg.salesflow.store.repository;


import com.portafolio.zomtg.salesflow.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    boolean existsById(UUID id);
   Optional<Store> findStoreById(UUID id);
   List<Store> findStoreByOwnerId(UUID id);




}
