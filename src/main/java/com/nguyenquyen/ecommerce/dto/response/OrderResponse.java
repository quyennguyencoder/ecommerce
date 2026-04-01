package com.nguyenquyen.ecommerce.dto.response;

import com.nguyenquyen.ecommerce.enums.Gender;
import com.nguyenquyen.ecommerce.enums.ShippingMethod;
import com.nguyenquyen.ecommerce.enums.OrderStatus;
import com.nguyenquyen.ecommerce.enums.PaymentStatus;
import com.nguyenquyen.ecommerce.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private String name;
    private Gender gender;
    private String phone;
    private String email;
    private String shippingAddress;
    private String note;
    private ShippingMethod shippingMethod;
    private BigDecimal shippingFee;
    private BigDecimal total;
    private OrderStatus status;
    private Long userId;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String paymentUrl;
    private Set<OrderDetailResponse> orderDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
