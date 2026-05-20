package com.portafolio.zomtg.salesflow.receipt.dto;

import java.util.UUID;

public class ReceiptDTO {
    private UUID saleId;
    private double total;

    public ReceiptDTO(UUID saleId, double total) {
        this.saleId = saleId;
        this.total = total;
    }
    public ReceiptDTO() {}

    public UUID getSaleId() {
        return saleId;
    }

    public void setSaleId(UUID saleId) {
        this.saleId = saleId;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
