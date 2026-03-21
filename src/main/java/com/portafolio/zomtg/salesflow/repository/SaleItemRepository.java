package com.portafolio.zomtg.salesflow.repository;

import com.portafolio.zomtg.salesflow.model.entities.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem,Long> {
    List<SaleItem> findBySaleId(UUID saleId);
}
