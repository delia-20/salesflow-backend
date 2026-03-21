package com.portafolio.zomtg.salesflow.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

@Entity
@Table(name = "saleItem")
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID saleId;
    @Column(nullable = false)
    private  UUID productId;
    @Column(nullable = false)
    @Positive
    private int quantity;
    @Column(nullable = false)
    @Positive
    private double priceAtMoment;
    @Column(nullable = false)
    @Positive
    private double subtotal;

    public SaleItem(UUID saleId, UUID productId, int quantity, double priceAtMoment, double subtotal) {
        this.saleId = saleId;
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtMoment = priceAtMoment;
        this.subtotal = subtotal;
    }
    public SaleItem(){

    }

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

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtMoment() {
        return priceAtMoment;
    }

    public void setPriceAtMoment(double priceAtMoment) {
        this.priceAtMoment = priceAtMoment;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }
}
