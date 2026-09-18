package com.portafolio.zomtg.salesflow.users.dto;

import java.util.UUID;

public record EmployeeUpdateRequest(
        String name,
        String username,
        String surname,
        String password,
        UUID storeId
) {
}
