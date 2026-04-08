package com.nguyenquyen.ecommerce.dto.request;

import com.nguyenquyen.ecommerce.enums.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponCreateRequest {

    @NotBlank(message = "Mã coupon không được để trống")
    @Size(max = 50, message = "Mã coupon không được vượt quá 50 ký tự")
    private String code;

    @NotNull(message = "Loại giảm giá không được để trống")
    private DiscountType discountType; // PERCENTAGE, FIXED_AMOUNT

    @NotNull(message = "Giá trị giảm giá không được để trống")
    @DecimalMin(value = "0.00", message = "Giá trị giảm giá phải lớn hơn hoặc bằng 0")
    private BigDecimal discountValue;

    @Min(value = 0, message = "Giá trị đơn hàng tối thiểu phải lớn hơn hoặc bằng 0")
    private Integer minOrderValue;

    @Min(value = 0, message = "Giảm giá tối đa phải lớn hơn hoặc bằng 0")
    private Integer maxDiscount;

    @Min(value = 0, message = "Giới hạn sử dụng phải lớn hơn hoặc bằng 0")
    private Integer usageLimit;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDateTime startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDateTime endDate;

    @NotNull(message = "Trạng thái hoạt động không được để trống")
    private Boolean active;
}
