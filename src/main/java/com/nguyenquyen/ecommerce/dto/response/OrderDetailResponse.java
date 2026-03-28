package com.nguyenquyen.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {

    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private Long orderId;
    private Long variantId;
    private String variantSku;
    private String productName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
