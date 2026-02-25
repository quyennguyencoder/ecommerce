package com.nguyenquyen.ecommerce.dtos;


import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailDTO {
    private Long orderId;
    private Long productId;
    @Min(value = 0, message = "Price must be minimum 0")
    private Long price;
    @Min(value = 1, message = "Number of products must be minimum 1")
    private int numberOfProducts;
    @Min(value = 0, message = "Total money must be minimum 0")
    private Float totalMoney;
    private String color;
}
