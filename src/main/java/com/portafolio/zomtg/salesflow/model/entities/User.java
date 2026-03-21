
package com.portafolio.zomtg.salesflow.model.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.portafolio.zomtg.salesflow.model.enums.Role;
import jakarta.persistence.*;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {

    // --- Identificador ---
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String surname;
    @Enumerated(EnumType.STRING)
    private Role role; // OWNER, EMPLOYEE, CLIENT
    @Column(unique = true, nullable = false)
    private String username;
    @Column(unique = true, nullable = false)
    @Email(message = "Email cannot be empty")
    private String email;
    @Getter
    @Setter
   @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private UUID ownerId; // owner y empleado
    private UUID storeId; //empleado

    // --- Estado de usuario ---
    private boolean active = true;

    public User() {}

    public User(String name, String surname, Role role, String username, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.username = username;
        this.email = email;
        this.password = password;

    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public UUID getStoreId() {
        return storeId;
    }

    public void setStoreId(UUID storeId) {
        this.storeId = storeId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // --- Métodos de utilidad ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return name + " " + surname + " (" + role + ")";
    }
}
