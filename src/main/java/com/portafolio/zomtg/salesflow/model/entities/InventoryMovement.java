package com.portafolio.zomtg.salesflow.model.entities;

import com.portafolio.zomtg.salesflow.model.enums.TypeInventory;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;


import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="inventoryMovement")
public class InventoryMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID productId;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeInventory type;
    @Column(nullable = false)
    @Positive
    private int quantity;
    @Positive
    private int previousStock;
    @Positive
    private int newStock;
    @Column(nullable = false)
    private LocalDateTime dateTime;
    //@Column(nullable = false)
    private UUID referenceId;//venta=saleId,Restock-ownerId/employeId
    //sale =saleID, newProduct=ownerId, addProduct

    public InventoryMovement(UUID productId, TypeInventory type, int quantity, LocalDateTime dateTime, UUID referenceId, int previousStock, int newStock) {

        this.productId = productId;
        this.type = type;
        this.quantity = quantity;
        this.dateTime = dateTime;
        this.referenceId = referenceId;
        this.previousStock = previousStock;
        this.newStock = newStock;
    }
    public InventoryMovement() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public TypeInventory getType() {
        return type;
    }

    public void setType(TypeInventory type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public UUID getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(UUID referenceId) {
        this.referenceId = referenceId;
    }

    public int getPreviousStock() {
        return previousStock;
    }

    public void setPreviousStock(int previousStock) {
        this.previousStock = previousStock;
    }

    public int getNewStock() {
        return newStock;
    }

    public void setNewStock(int newStock) {
        this.newStock = newStock;
    }


    @Override
    public String toString() {
        return "InventoryMovement [id=" + id
                + ", productId=" + productId
                + ", type=" + type
                + ", quantity=" + quantity
                + ", dateTime=" + dateTime.getYear()+"-"+dateTime.getMonth()+"-"+dateTime.getDayOfMonth()
                + ", referenceId=" + referenceId
                + ", previousStock=" + previousStock
                + ", newStock=" + newStock + "]";
    }


}
