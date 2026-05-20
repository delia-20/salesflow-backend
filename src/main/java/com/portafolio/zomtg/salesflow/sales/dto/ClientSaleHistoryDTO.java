package com.portafolio.zomtg.salesflow.sales.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ClientSaleHistoryDTO {
    private String saleNumber;
    private double total;
    private LocalDateTime datetime;
    private List<SalesItemsRequestDTO> items;

    public ClientSaleHistoryDTO(String saleNumber, double total, LocalDateTime datetime, List<SalesItemsRequestDTO> items) {
        this.saleNumber = saleNumber;
        this.total = total;
        this.datetime = datetime;
        this.items = items;
    }

    public String getSaleNumber() {
        return saleNumber;
    }

    public void setSaleNumber(String saleNumber) {
        this.saleNumber = saleNumber;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public List<SalesItemsRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<SalesItemsRequestDTO> items) {
        this.items = items;
    }
}
