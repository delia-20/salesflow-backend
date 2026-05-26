package com.portafolio.zomtg.salesflow.store.dto;

import java.util.UUID;

public record StoreRequest(
        String name,
        String description,
        String category,
        String address,
        String phone,
        String email,
        String password,
        String website,
        UUID ownerId

) {
}
