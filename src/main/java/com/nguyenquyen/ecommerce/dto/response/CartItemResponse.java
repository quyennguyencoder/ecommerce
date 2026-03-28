package com.nguyenquyen.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Long id;
    private Integer quantity;
    private Long variantId;
    private String variantSku;
    private String productName;
    private BigDecimal price;
    private String image;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
