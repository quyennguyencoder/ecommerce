package com.nguyenquyen.ecommerce.dtos.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductListResponse {
    private int totalPages;
    private List<ProductResponse> products;
}
