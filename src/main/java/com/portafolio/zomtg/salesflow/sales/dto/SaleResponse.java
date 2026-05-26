package com.portafolio.zomtg.salesflow.sales.dto;

import com.portafolio.zomtg.salesflow.sales.enums.PaymentMethod;
import com.portafolio.zomtg.salesflow.sales.enums.StatusSale;

import java.time.LocalDateTime;
import java.util.UUID;

public record SaleResponse(
        UUID id,
        UUID storeId,
        UUID employeeId,
        LocalDateTime datetime,
        double total,
        StatusSale status,
        PaymentMethod paymentMethod,
        UUID clientId,
        String saleNumber
) {
}
