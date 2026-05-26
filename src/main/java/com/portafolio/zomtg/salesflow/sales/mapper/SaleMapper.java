package com.portafolio.zomtg.salesflow.sales.mapper;

import com.portafolio.zomtg.salesflow.sales.dto.SaleResponse;
import com.portafolio.zomtg.salesflow.sales.entity.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {
    public SaleResponse toSaleResponse(Sale sale) {
        SaleResponse response= new SaleResponse(
                sale.getId(),
                sale.getStoreId(),
                sale.getEmployeeId(),
                sale.getDatetime(),
                sale.getTotal(),
                sale.getStatus(),
                sale.getPaymentMethod(),
                sale.getClientId(),
                sale.getSaleNumber()
        );
        return response;
    }


}
