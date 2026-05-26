package com.portafolio.zomtg.salesflow.users.dto;

import com.portafolio.zomtg.salesflow.users.enums.Role;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String surname,
        String username,
        String email,
        Role role,
        UUID ownerId,
        UUID storeId,
        boolean active

) {
}
