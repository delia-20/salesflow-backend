package com.portafolio.zomtg.salesflow.inventory.repository;

import com.portafolio.zomtg.salesflow.inventory.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryMovement, UUID> {

    List<InventoryMovement> findByProductId(UUID productId);
}
