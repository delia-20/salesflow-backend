package com.portafolio.zomtg.salesflow.model.entities;



import com.portafolio.zomtg.salesflow.model.enums.PaymentMethod;
import com.portafolio.zomtg.salesflow.model.enums.StatusSale;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name="sale")
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID storeId;
    @Column(nullable = false)
    private UUID employeeId;
    private LocalDateTime datetime;
    @Column(nullable = false)
    @PositiveOrZero
    private double total;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusSale status;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private UUID clientId;
    private String saleNumber;



    public Sale(){}

    public Sale(UUID storeId, UUID employeeId, double total, StatusSale status, PaymentMethod paymentMethod, UUID clientId) {
        this.storeId = storeId;
        this.employeeId = employeeId;
        this.total = total;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.clientId = clientId;
    }
    public Sale(UUID storeId, UUID employeeId, double total, StatusSale status, PaymentMethod paymentMethod) {
        this.storeId = storeId;
        this.employeeId = employeeId;
        this.total = total;
        this.status = status;
        this.paymentMethod = paymentMethod;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public UUID getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(UUID employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public StatusSale getStatus() {
        return status;
    }

    public void setStatus(StatusSale status) {
        this.status = status;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(UUID clientId) {
        this.clientId = clientId;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleNumber='" + saleNumber+
                ", storeId=" + storeId +
                ", employeeId=" + employeeId +
                ", datetime=" + datetime +
                ", total=" + total +
                ", status=" + status +
                ", paymentMethod=" + paymentMethod +
                ", clientId=" + clientId +
                 '\'' +
                '}';
    }
}
