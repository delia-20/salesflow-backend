package com.portafolio.zomtg.salesflow.store.dto;

import java.util.UUID;

public record StoreResponse(
        UUID id,
        String name,
        String description,
        String category,
        String address,
        String phone,
        String email,
        int numemployees,
        int numProducts,
        int numTotalItems,
        String website,
        UUID ownerId
) {
}
