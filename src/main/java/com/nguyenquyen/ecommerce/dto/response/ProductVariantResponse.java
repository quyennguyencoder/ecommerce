package com.nguyenquyen.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {

    private Long id;
    private String sku;
    private BigDecimal price;
    private Integer stock;
    private String image;
    private Long productId;
    private String productName;
    private List<Long> attributeValueIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
