package com.nguyenquyen.ecommerce.dto.request.shippingDetail;

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
public class ShippingDetailUpdateRequest {

    @NotBlank(message = "Tên người nhận không được để trống")
    @Size(max = 255, message = "Tên người nhận không được vượt quá 255 ký tự")
    private String name;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    @Size(max = 255, message = "Địa chỉ giao hàng không được vượt quá 255 ký tự")
    private String shippingAddress;

    @Size(max = 50, message = "Phương thức vận chuyển không được vượt quá 50 ký tự")
    private String shippingMethod;

    @Size(max = 100, message = "Đơn vị vận chuyển không được vượt quá 100 ký tự")
    private String carrier;

    @DecimalMin(value = "0.00", message = "Phí vận chuyển phải lớn hơn hoặc bằng 0")
    private BigDecimal shippingFee;
}
