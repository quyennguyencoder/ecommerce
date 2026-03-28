package com.nguyenquyen.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String shippingAddress;
    private String note;
    private String shippingMethod;
    private String carrier;
    private BigDecimal shippingFee;
    private BigDecimal total;
    private String status;
    private Long userId;
    private Long couponId;
    private Set<OrderDetailResponse> orderDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
