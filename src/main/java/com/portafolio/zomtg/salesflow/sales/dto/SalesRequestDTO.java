package com.portafolio.zomtg.salesflow.sales.dto;

import com.portafolio.zomtg.salesflow.sales.enums.PaymentMethod;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.util.List;
import java.util.UUID;

public class SalesRequestDTO {
    private UUID storeId;

    private UUID clientId;
    private List<SalesItemsRequestDTO> items;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;


    public SalesRequestDTO(UUID storeId,
                           List<SalesItemsRequestDTO> items,
                           PaymentMethod paymentMethod,
                           UUID clientId) {
        this.storeId = storeId;
        this.items = items;
        this.paymentMethod = paymentMethod;

        this.clientId = clientId;
    }
    public SalesRequestDTO(UUID storeId,
                           List<SalesItemsRequestDTO> items,
                           PaymentMethod paymentMethod) {
        this.storeId = storeId;
        this.items = items;
        this.paymentMethod = paymentMethod;
    }
    public SalesRequestDTO(){}

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public List<SalesItemsRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<SalesItemsRequestDTO> items) {
        this.items = items;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    @Override
    public String toString() {
        return "Store [storeId=" + storeId + ", items=" + items.toString() + ", paymentMethod=" + paymentMethod + "]";
    }
}
