package com.portafolio.zomtg.salesflow.repository;

import com.portafolio.zomtg.salesflow.model.entities.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {

}
