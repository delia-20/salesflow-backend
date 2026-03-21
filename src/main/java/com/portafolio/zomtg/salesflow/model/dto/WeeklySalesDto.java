package com.portafolio.zomtg.salesflow.model.dto;

import java.time.LocalDateTime;

public class WeeklySalesDto {
    private LocalDateTime week;
    private Double total;

    public WeeklySalesDto(LocalDateTime week, Double total) {
        this.week = week;
        this.total = total;
    }

    public LocalDateTime getWeek() {
        return week;
    }

    public Double getTotal() {
        return total;
    }
}
