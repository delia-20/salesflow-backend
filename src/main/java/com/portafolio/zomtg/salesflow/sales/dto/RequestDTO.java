package com.portafolio.zomtg.salesflow.sales.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class RequestDTO {
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate day;
    public RequestDTO(LocalDate localDate) {
        this.day = localDate;
    }
    public RequestDTO() {}
    public LocalDate getDay() {
        return day;
    }
    public void setDay(LocalDate day) {
        this.day = day;
    }
}
