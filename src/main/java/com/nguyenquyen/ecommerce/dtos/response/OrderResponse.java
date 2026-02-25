package com.nguyenquyen.ecommerce.dtos.response;

import com.nguyenquyen.ecommerce.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse extends BaseResponse{
    private Long id;
    private Long userId;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String note;
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Float totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private LocalDate shippingDate;
    private String trackingNumber;
    private String paymentMethod;
    private Boolean active;
}
