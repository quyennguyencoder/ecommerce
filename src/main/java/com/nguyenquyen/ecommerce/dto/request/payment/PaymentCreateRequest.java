package com.nguyenquyen.ecommerce.dto.request.payment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentCreateRequest {

    @Size(max = 255, message = "Transaction ID không được vượt quá 255 ký tự")
    private String transactionId;

    @NotBlank(message = "Phương thức thanh toán không được để trống")
    @Size(max = 50, message = "Phương thức thanh toán không được vượt quá 50 ký tự")
    private String paymentMethod;

    @Size(max = 50, message = "Trạng thái thanh toán không được vượt quá 50 ký tự")
    private String paymentStatus;
}
