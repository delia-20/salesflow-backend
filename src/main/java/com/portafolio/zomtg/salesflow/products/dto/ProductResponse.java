package com.portafolio.zomtg.salesflow.products.dto;

import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String description,
        double price,
        String image,
        String category,
        int existence,
        boolean active,
        UUID storeId
) {
}
