package com.nguyenquyen.ecommerce.dto.request.order;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreateRequest {

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    private String note;

    @NotNull(message = "Tổng tiền không được để trống")
    @DecimalMin(value = "0.00", message = "Tổng tiền phải lớn hơn hoặc bằng 0")
    private BigDecimal total;

    private Long couponId;
}
