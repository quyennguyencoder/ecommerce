package com.nguyenquyen.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCalculationResponse {

    private BigDecimal subTotal;

    private BigDecimal discountAmount;

    private BigDecimal shippingFee;

    private BigDecimal finalTotal;

    private Boolean isCouponValid;
}
