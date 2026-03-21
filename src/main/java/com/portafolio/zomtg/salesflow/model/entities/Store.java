package com.portafolio.zomtg.salesflow.model.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Entity
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column( nullable = false)
    private String description;
    @Column(nullable = false)
    private String category;
    private String address;
    @Column(unique = true, nullable = false)
    private String phone;
    @Column(nullable = false)
    @Email(message = "email cannot be empty")
    private String email;
    
    private int numEmployees;
    private int numProducts;//cantidad en categoria no cantidad total de items
    private int numTotalItems;
    @Getter
    @Setter
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String website;
 //
    @Column(nullable = false)
    private UUID ownerId;

    public Store() {

    }

    public Store(String name, String description, String category, String address, String phone, String email, String password, String website) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.website = website;
        this.password = password;
        this.numEmployees = 0;
        this.numProducts = 0;
        this.numTotalItems = 0;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public int getNumEmployees() {
        return numEmployees;
    }

    public void setNumEmployees(int numEmployees) {
        this.numEmployees = numEmployees;
    }

    public int getNumProducts() {
        return numProducts;
    }

    public void setNumProducts(int numProducts) {
        this.numProducts = numProducts;
    }

    public int getNumTotalItems() {
        return numTotalItems;
    }

    public void setNumTotalItems(int numTotalItems) {
        this.numTotalItems = numTotalItems;
    }
}
