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
        String terminalCode = vnPayConfig.getTmnCode();

        // Lấy và xử lý IP thực của client để không văng lỗi 15 của VNPAY
        String clientIpAddress = vnPayUtils.getIpAddress(securityUtil.getCurrentRequest());
        if (clientIpAddress != null && clientIpAddress.contains(",")) {
            clientIpAddress = clientIpAddress.split(",")[0].trim();
        }
        // VNPAY chỉ chấp nhận IP độ dài tối đa 15 ký tự (IPv4). Nếu là IPv6 đành fallback về IP nội bộ.
        if (clientIpAddress == null || clientIpAddress.length() > 15 || clientIpAddress.contains(":")) {
            clientIpAddress = "127.0.0.1";
        }

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
        params.put("vnp_IpAddr", clientIpAddress);

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

        // Sử dụng US_ASCII chuẩn hóa theo đúng code mẫu Java của VNPAY
        String encodeCharset = StandardCharsets.US_ASCII.toString();

        try {
            for (Iterator<String> iterator = sortedFieldNames.iterator(); iterator.hasNext();) {
                String fieldName = iterator.next();
                String fieldValue = params.get(fieldName);

                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, encodeCharset));
                    queryData.append(URLEncoder.encode(fieldName, encodeCharset))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, encodeCharset));
                    if (iterator.hasNext()) {
                        hashData.append('&');
                        queryData.append('&');
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Lỗi mã hóa URL VNPAY", e);
        }

        String secureHash = vnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData.toString());
        queryData.append("&vnp_SecureHash=").append(secureHash);

        return PaymentUrlCreateResponse.builder()
                .paymentUrl(vnPayConfig.getPaymentUrl() + "?" + queryData)
                .build();
    }

    @Override
    @Transactional
    public TransactionResponse queryTransaction(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch với ID: " + transactionId));

        if (payment.getTransactionStatus() != TransactionStatus.PENDING) {
            return buildTransactionResponse(payment);
        }

        VNPayQueryResponse vnPayResponse = queryVNPayTransaction(payment);

        if (vnPayResponse != null && "00".equals(vnPayResponse.getRspCode())) {
            updatePaymentStatus(payment, vnPayResponse);
            updateOrderStatus(payment.getOrder(), payment);
            paymentRepository.save(payment);
            orderRepository.save(payment.getOrder());
        } else {
            throw new RuntimeException("Không thể lấy thông tin giao dịch từ VNPay: " +
                    (vnPayResponse != null ? vnPayResponse.getMessage() : "Unknown error"));
        }

        return buildTransactionResponse(payment);
    }

    private TransactionResponse buildTransactionResponse(Payment payment) {
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

    private VNPayQueryResponse queryVNPayTransaction(Payment payment) {
        try {
            String queryUrl = vnPayConfig.getApiUrl();

            String vnp_RequestId = vnPayUtils.getRandomNumber(10);
            String vnp_Version = "2.1.0";
            String vnp_Command = "querydr";
            String vnp_TmnCode = vnPayConfig.getTmnCode();
            String vnp_TxnRef = payment.getTransactionId();
            String vnp_OrderInfo = "Truy van giao dich " + vnp_TxnRef;

            // Tương tự, cắt gọt IP ở hàm gọi API Query
            String vnp_IpAddr = vnPayUtils.getIpAddress(securityUtil.getCurrentRequest());
            if (vnp_IpAddr != null && vnp_IpAddr.contains(",")) {
                vnp_IpAddr = vnp_IpAddr.split(",")[0].trim();
            }
            if (vnp_IpAddr == null || vnp_IpAddr.length() > 15 || vnp_IpAddr.contains(":")) {
                vnp_IpAddr = "127.0.0.1";
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            String vnp_CreateDate = formatter.format(new Date());

            java.time.ZonedDateTime zonedDateTime = payment.getCreatedAt()
                    .atZone(java.time.ZoneId.of("Asia/Ho_Chi_Minh"));
            String vnp_TransactionDate = formatter.format(java.util.Date.from(zonedDateTime.toInstant()));

            String hashData = vnp_RequestId + "|" + vnp_Version + "|" + vnp_Command + "|"
                    + vnp_TmnCode + "|" + vnp_TxnRef + "|" + vnp_TransactionDate + "|"
                    + vnp_CreateDate + "|" + vnp_IpAddr + "|" + vnp_OrderInfo;

            String secureHash = vnPayUtils.hmacSHA512(vnPayConfig.getHashSecret(), hashData);

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

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

            return restTemplate.postForObject(queryUrl, entity, VNPayQueryResponse.class);

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi gọi VNPay API: " + e.getMessage(), e);
        }
    }

    private void updatePaymentStatus(Payment payment, VNPayQueryResponse vnPayResponse) {
        String vnpayStatus = vnPayResponse.getTransactionStatus();
        if ("00".equals(vnpayStatus)) {
            payment.setTransactionStatus(TransactionStatus.SUCCESS);
        } else {
            payment.setTransactionStatus(TransactionStatus.FAILED);
        }
    }

    private void updateOrderStatus(Order order, Payment payment) {
        if (payment.getTransactionStatus() == TransactionStatus.SUCCESS) {
            order.setPaymentStatus(PaymentStatus.PAID);
            if (order.getStatus() == OrderStatus.PENDING) {
                order.setStatus(OrderStatus.PROCESSING);
            }
        } else {
            order.setPaymentStatus(PaymentStatus.UNPAID);
        }
    }
}