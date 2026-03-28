package com.nguyenquyen.ecommerce.dto.request.order;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateRequest {

    @Size(max = 255, message = "Tên người nhận không được vượt quá 255 ký tự")
    private String name;

    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;

    @Size(max = 255, message = "Địa chỉ giao hàng không được vượt quá 255 ký tự")
    private String shippingAddress;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;

    @Size(max = 50, message = "Phương thức vận chuyển không được vượt quá 50 ký tự")
    private String shippingMethod;

    @Size(max = 100, message = "Đơn vị vận chuyển không được vượt quá 100 ký tự")
    private String carrier;

    @DecimalMin(value = "0.00", message = "Phí vận chuyển phải lớn hơn hoặc bằng 0")
    private BigDecimal shippingFee;

    @DecimalMin(value = "0.00", message = "Tổng tiền phải lớn hơn hoặc bằng 0")
    private BigDecimal total;

    @Size(max = 50, message = "Trạng thái không được vượt quá 50 ký tự")
    private String status; // PENDING, CONFIRMED, SHIPPING, DELIVERED, CANCELLED

    private Long couponId;
}
