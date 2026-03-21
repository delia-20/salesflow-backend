package com.portafolio.zomtg.salesflow.model.dto;

import lombok.*;

import java.util.UUID;

@Data
public class UserDTO {
    @Getter

    private UUID id;
    private String name;

    private String  surname;

    private String role;



}
