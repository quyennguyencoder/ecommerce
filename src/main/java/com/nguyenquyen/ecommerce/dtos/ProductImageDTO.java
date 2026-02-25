package com.nguyenquyen.ecommerce.dtos;

import com.nguyenquyen.ecommerce.models.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {
    private String imageUrl;
    private Long productId;
}
