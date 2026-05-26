package com.portafolio.zomtg.salesflow.users.dto;

import com.portafolio.zomtg.salesflow.users.enums.Role;

import java.util.UUID;

public record RegisterUserRequest(
        String name,
        String username,
        String surname,
        String email,
        String password,
        Role role,
        UUID ownerId,
        UUID storeId

) {


}
