package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.PaymentUrlCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.PaymentUrlCreateResponse;
import com.nguyenquyen.ecommerce.dto.response.TransactionResponse;


public interface IPaymentService {
    PaymentUrlCreateResponse createPaymentUrl(PaymentUrlCreateRequest paymentCreateRequest);

    TransactionResponse queryTransaction(String transactionId);
}

