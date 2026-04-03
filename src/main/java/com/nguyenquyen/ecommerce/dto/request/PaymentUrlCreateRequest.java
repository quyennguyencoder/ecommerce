package com.nguyenquyen.ecommerce.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentUrlCreateRequest {
    private Long orderId;
    private String bankCode;
    private String language;
}
