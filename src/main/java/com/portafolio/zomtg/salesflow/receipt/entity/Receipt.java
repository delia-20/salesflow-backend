package com.portafolio.zomtg.salesflow.receipt.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="receipt")
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private UUID saleId;
    @Column(nullable = false)
    private UUID folio;
    @Column(nullable = false)
    private LocalDateTime dateTime;
    @Column(nullable = false)
    @Positive
    private double total;
    @Column(nullable = false)
    @Positive
    private double tax;
    @Column(nullable = false)
    private Long saleNumber;
    private  UUID clientId;


    public Receipt(UUID saleId, UUID folio, LocalDateTime dateTime, double total, double tax, Long saleNumber) {
        this.saleId = saleId;
        this.folio = folio;
        this.dateTime = dateTime;
        this.total = total;
        this.tax = tax;
        this.saleNumber = saleNumber;
    }
    public Receipt() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getSaleId() {
        return saleId;
    }

    public void setSaleId(UUID saleId) {
        this.saleId = saleId;
    }

    public UUID getFolio() {
        return folio;
    }

    public void setFolio(UUID folio) {
        this.folio = folio;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }
    public Long getSaleNumber() {
        return saleNumber;
    }
    public void setSaleNumber(Long saleNumber) {
        this.saleNumber = saleNumber;
    }

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }
}
