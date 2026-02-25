package com.nguyenquyen.ecommerce.dtos.response;

import jakarta.persistence.MappedSuperclass;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class OrderDetailResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private Float price;
    private int numberOfProducts;
    private Float totalMoney;
    private String color;
}
