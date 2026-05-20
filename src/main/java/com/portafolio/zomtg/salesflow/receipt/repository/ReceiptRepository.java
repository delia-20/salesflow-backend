package com.portafolio.zomtg.salesflow.receipt.repository;

import com.portafolio.zomtg.salesflow.receipt.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {

}
