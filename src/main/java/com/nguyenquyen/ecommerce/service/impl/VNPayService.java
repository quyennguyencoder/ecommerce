package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.config.VNPayConfig;
import com.nguyenquyen.ecommerce.util.VNPayUtil;
import com.nguyenquyen.ecommerce.dto.request.PaymentUrlCreateRequest;
import com.nguyenquyen.ecommerce.dto.response.PaymentUrlCreateResponse;
import com.nguyenquyen.ecommerce.dto.response.TransactionResponse;
import com.nguyenquyen.ecommerce.dto.response.VNPayQueryResponse;
import com.nguyenquyen.ecommerce.enums.OrderStatus;
import com.nguyenquyen.ecommerce.enums.PaymentMethod;
import com.nguyenquyen.ecommerce.enums.PaymentStatus;
import com.nguyenquyen.ecommerce.enums.TransactionStatus;
import com.nguyenquyen.ecommerce.model.Order;
import com.nguyenquyen.ecommerce.model.Payment;
import com.nguyenquyen.ecommerce.repository.OrderRepository;
import com.nguyenquyen.ecommerce.repository.PaymentRepository;
import com.nguyenquyen.ecommerce.service.IPaymentService;
import com.nguyenquyen.ecommerce.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayService implements IPaymentService {
    private final VNPayConfig vnPayConfig;
    private final VNPayUtil vnPayUtils;
    private final OrderRepository orderRepository;
    private final SecurityUtil securityUtil;
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;

    @Transactional
    public PaymentUrlCreateResponse createPaymentUrl(PaymentUrlCreateRequest paymentCreateRequest) {
        Order existingOrder = orderRepository.findById(paymentCreateRequest.getOrderId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng!"));
        Payment newPayment = Payment.builder()
                .transactionId(vnPayUtils.getRandomNumber(8))
                .paymentMethod(PaymentMethod.VNPAY)
                .transactionStatus(TransactionStatus.PENDING)
                .order(existingOrder)
                .build();
        paymentRepository.save(newPayment);

        String version = "2.1.0";
        String command = "pay";
        String orderType = "other";

        // Tổng tiền nhân 100
        long amount = existingOrder.getTotal().multiply(new BigDecimal(100)).longValue();
        String bankCode = paymentCreateRequest.getBankCode();

        String transactionReference = newPayment.getTransactionId();

        String clientIpAddress = vnPayUtils.getIpAddress(securityUtil.getCurrentRequest());
        String terminalCode = vnPayConfig.getTmnCode();

        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", version);
        params.put("vnp_Command", command);
        params.put("vnp_TmnCode", terminalCode);
        params.put("vnp_Amount", String.valueOf(amount));
        params.put("vnp_CurrCode", "VND");

        if (bankCode != null && !bankCode.isEmpty()) {
            params.put("vnp_BankCode", bankCode);
        }
        params.put("vnp_TxnRef", transactionReference);
        params.put("vnp_OrderInfo", "ThanhToan");
        params.put("vnp_OrderType", orderType);

        String locale = paymentCreateRequest.getLanguage();
        if (locale != null && !locale.isEmpty()) {
            params.put("vnp_Locale", locale);
        } else {
            params.put("vnp_Locale", "vn");
        }

        params.put("vnp_ReturnUrl", vnPayConfig.getReturnUrl());
        params.put("vnp_IpAddr", "127.0.0.1");

        // SỬA: Dùng đúng TimeZone của Việt Nam
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String createdDate = dateFormat.format(calendar.getTime());
        params.put("vnp_CreateDate", createdDate);

        calendar.add(Calendar.MINUTE, 15);
        String expirationDate = dateFormat.format(calendar.getTime());
        params.put("vnp_ExpireDate", expirationDate);

        List<String> sortedFieldNames = new ArrayList<>(params.keySet());
        Collections.sort(sortedFieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder queryData = new StringBuilder();

        for (Iterator<String> iterator = sortedFieldNames.iterator(); iterator.hasNext();) {
            String fieldName = iterator.next();
            String fieldValue = params.get(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                // SỬA: Nên dùng UTF-8 chuẩn
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                queryData.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                if (iterator.hasNext()) {
                    hashData.append('&');
                    queryData.append('&');
                }
            }
        }

        String secureHash = vnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        queryData.append("&vnp_SecureHash=").append(secureHash);

        System.out.println("paymentUrl: " + vnPayConfig.getPaymentUrl() + "?" + queryData);

        return PaymentUrlCreateResponse.builder()
                .paymentUrl(vnPayConfig.getPaymentUrl() + "?" + queryData)
                .build();
    }

    @Override
    @Transactional
    public TransactionResponse queryTransaction(String transactionId) {
        // Lấy Payment từ database
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch với ID: " + transactionId));

        // Kiểm tra nếu Payment đã được xử lý rồi (không phải PENDING) thì không gọi API lần nữa
        // Điều này tránh request bị trùng lặp từ VNPay
        if (payment.getTransactionStatus() != TransactionStatus.PENDING) {
            System.out.println("Payment đã được xử lý rồi, trạng thái hiện tại: " + payment.getTransactionStatus());
            return TransactionResponse.builder()
                    .id(payment.getId())
                    .transactionId(payment.getTransactionId())
                    .paymentMethod(payment.getPaymentMethod())
                    .transactionStatus(payment.getTransactionStatus())
                    .orderId(payment.getOrder().getId())
                    .createdAt(payment.getCreatedAt())
                    .updatedAt(payment.getUpdatedAt())
                    .build();
        }

        // Gọi VNPay API để lấy thông tin giao dịch
        VNPayQueryResponse vnPayResponse = queryVNPayTransaction(payment);
        System.out.println("VNPay API Response: " + vnPayResponse);
        // Xử lý response từ VNPay
        if (vnPayResponse != null && "00".equals(vnPayResponse.getRspCode())) {
            // Cập nhật status của Payment dựa trên response từ VNPay
            updatePaymentStatus(payment, vnPayResponse);

            // Cập nhật Order dựa trên trạng thái thanh toán
            updateOrderStatus(payment.getOrder(), payment);

            // Lưu lại các thay đổi
            paymentRepository.save(payment);
            orderRepository.save(payment.getOrder());
        } else {
            throw new RuntimeException("Không thể lấy thông tin giao dịch từ VNPay: " +
                    (vnPayResponse != null ? vnPayResponse.getMessage() : "Unknown error"));
        }

        return TransactionResponse.builder()
                .id(payment.getId())
                .transactionId(payment.getTransactionId())
                .paymentMethod(payment.getPaymentMethod())
                .transactionStatus(payment.getTransactionStatus())
                .orderId(payment.getOrder().getId())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    /**
     * Gọi VNPay API để lấy thông tin giao dịch
     */
    private VNPayQueryResponse queryVNPayTransaction(Payment payment) {
        try {
            // Lưu ý: URL của API Query thường kết thúc bằng /merchant_webapi/api/transaction
            String queryUrl = vnPayConfig.getApiUrl();

            String vnp_RequestId = vnPayUtils.getRandomNumber(10);
            String vnp_Version = "2.1.0";
            String vnp_Command = "querydr";
            String vnp_TmnCode = vnPayConfig.getTmnCode();
            String vnp_TxnRef = payment.getTransactionId();
            String vnp_OrderInfo = "Truy van giao dich " + vnp_TxnRef;
            // Lấy IP thực từ request hiện tại
            String vnp_IpAddr = "127.0.0.1";

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            // Thời gian lúc gọi API Query
            String vnp_CreateDate = formatter.format(new Date());

            // Thời gian lúc khởi tạo đơn hàng thanh toán
            // Convert LocalDateTime sang Date với timezone Việt Nam
            java.time.ZonedDateTime zonedDateTime = payment.getCreatedAt()
                    .atZone(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
            String vnp_TransactionDate = formatter.format(java.util.Date.from(zonedDateTime.toInstant()));

            // 1. Tạo HashData bằng cách nối các biến với dấu | theo đúng thứ tự VNPay yêu cầu
            String hashData = vnp_RequestId + "|" + vnp_Version + "|" + vnp_Command + "|"
                    + vnp_TmnCode + "|" + vnp_TxnRef + "|" + vnp_TransactionDate + "|"
                    + vnp_CreateDate + "|" + vnp_IpAddr + "|" + vnp_OrderInfo;

            String secureHash = vnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData);

            // 2. Xây dựng Request Body (Định dạng JSON)
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("vnp_RequestId", vnp_RequestId);
            requestBody.put("vnp_Version", vnp_Version);
            requestBody.put("vnp_Command", vnp_Command);
            requestBody.put("vnp_TmnCode", vnp_TmnCode);
            requestBody.put("vnp_TxnRef", vnp_TxnRef);
            requestBody.put("vnp_OrderInfo", vnp_OrderInfo);
            requestBody.put("vnp_TransactionDate", vnp_TransactionDate);
            requestBody.put("vnp_CreateDate", vnp_CreateDate);
            requestBody.put("vnp_IpAddr", vnp_IpAddr);
            requestBody.put("vnp_SecureHash", secureHash);

            // 3. Cấu hình Headers là Application/JSON
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            // 4. Gọi API
            return restTemplate.postForObject(queryUrl, entity, VNPayQueryResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi VNPay API: " + e.getMessage(), e);
        }
    }


    private void updatePaymentStatus(Payment payment, VNPayQueryResponse vnPayResponse) {
        String vnpayStatus = vnPayResponse.getTransactionStatus();

        // VNPay status code: 00 = Success, other = failed
        if ("00".equals(vnpayStatus)) {
            payment.setTransactionStatus(TransactionStatus.SUCCESS);
        } else {
            payment.setTransactionStatus(TransactionStatus.FAILED);
        }
    }


    private void updateOrderStatus(Order order, Payment payment) {
        if (payment.getTransactionStatus() == TransactionStatus.SUCCESS) {
            order.setPaymentStatus(PaymentStatus.PAID);
            // Có thể tự động chuyển status order nếu cần
            if (order.getStatus() == OrderStatus.PENDING) {
                order.setStatus(OrderStatus.PROCESSING);
            }
        } else if (payment.getTransactionStatus() == TransactionStatus.FAILED) {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        } else {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }
    }
}