package com.portafolio.zomtg.salesflow.model.dto;

import jakarta.persistence.Column;

import java.util.UUID;

public class ProductDTO {
    private String name;
    private String description;
    private Double price;
    private String category;
    private Integer existence;

    public ProductDTO(String name, String description, Double price, String category, Integer existence) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.existence = existence;
    }

    public ProductDTO() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getExistence() {
        return existence;
    }

    public void setExistence(Integer existence) {
        this.existence = existence;
    }
}
