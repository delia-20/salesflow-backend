package com.portafolio.zomtg.salesflow.sales.dto;

import java.util.UUID;

public class SalesItemsRequestDTO {
    private UUID productId;
    private int quantity;
    private String productName;
    private double salePrice;
    private double totalPrice;

    public SalesItemsRequestDTO(UUID productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
    public SalesItemsRequestDTO() {

    }
    public SalesItemsRequestDTO(UUID productId, int quantity, double salePrice,double totalPrice,String productName) {
        this.productId = productId;
        this.quantity = quantity;
        this.salePrice = salePrice;
        this.totalPrice = totalPrice;
        this.productName = productName;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "SalesItemsRequestDTO [productId=" + productId + ", quantity=" + quantity +
                ", productName=" + productName + ", salePrice=" + salePrice +  ", totalPrice=" + totalPrice + "]";
    }
}
