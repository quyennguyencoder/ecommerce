
package com.nguyenquyen.ecommerce.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class VNPayConfig {

    @Value("${vnpay.tmn-code}")
    private String tmnCode;

    @Value("${vnpay.hash-secret}")
    private String hashSecret;

    @Value("${vnpay.payment-url}")
    private String paymentUrl;

    @Value("${vnpay.client-redirect-uri}")
    private String returnUrl;

    @Value("${vnpay.server-webhook-uri}")
    private String ipnUrl;

    @Value("${vnpay.api-url}")
    private String apiUrl;
}
