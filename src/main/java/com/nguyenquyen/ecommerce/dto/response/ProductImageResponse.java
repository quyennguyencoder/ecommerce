package com.nguyenquyen.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImageResponse {

    private Long id;
    private String imageUrl;
    private Long productId;
    private String productName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
