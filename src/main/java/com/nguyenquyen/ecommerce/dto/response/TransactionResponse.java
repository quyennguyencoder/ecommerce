package com.nguyenquyen.ecommerce.dto.response;

import com.nguyenquyen.ecommerce.enums.PaymentMethod;
import com.nguyenquyen.ecommerce.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private PaymentMethod paymentMethod;
    private TransactionStatus transactionStatus;
    private Long orderId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
