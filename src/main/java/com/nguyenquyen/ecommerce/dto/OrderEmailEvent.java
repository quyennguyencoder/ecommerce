package com.nguyenquyen.ecommerce.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEmailEvent implements Serializable {
    private Long orderId;
    private String customerEmail;
    private String customerName;
    private BigDecimal totalAmount;
}