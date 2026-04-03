package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.PaymentUrlCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.PaymentUrlCreateResponse;
import com.nguyenquyen.ecommerce.dto.response.TransactionResponse;
import com.nguyenquyen.ecommerce.service.IPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.prefix}/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IPaymentService paymentService;


    @PostMapping("/create-payment-url")
    public ResponseEntity<ApiResponse<PaymentUrlCreateResponse>> createPaymentUrl(
            @Valid @RequestBody PaymentUrlCreateRequest request) {

        PaymentUrlCreateResponse paymentUrlResponse = paymentService.createPaymentUrl(request);

        ApiResponse<PaymentUrlCreateResponse> response = ApiResponse.<PaymentUrlCreateResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo payment thành công")
                .data(paymentUrlResponse)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/query/{transactionId}")
    public ResponseEntity<ApiResponse<TransactionResponse>> queryTransaction(
            @PathVariable String transactionId) {

        TransactionResponse transaction = paymentService.queryTransaction(transactionId);

        ApiResponse<TransactionResponse> response = ApiResponse.<TransactionResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin giao dịch thành công")
                .data(transaction)
                .build();

        return ResponseEntity.ok(response);
    }
}
