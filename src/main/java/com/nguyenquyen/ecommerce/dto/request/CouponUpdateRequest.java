package com.nguyenquyen.ecommerce.dto.request;

import com.nguyenquyen.ecommerce.enums.DiscountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class CouponUpdateRequest {

    @Size(max = 50, message = "Mã coupon không được vượt quá 50 ký tự")
    private String code;

    private DiscountType discountType; // PERCENTAGE, FIXED_AMOUNT

    @DecimalMin(value = "0.00", message = "Giá trị giảm giá phải lớn hơn hoặc bằng 0")
    private BigDecimal discountValue;

    @Min(value = 0, message = "Giá trị đơn hàng tối thiểu phải lớn hơn hoặc bằng 0")
    private Integer minOrderValue;

    @Min(value = 0, message = "Giảm giá tối đa phải lớn hơn hoặc bằng 0")
    private Integer maxDiscount;

    @Min(value = 0, message = "Giới hạn sử dụng phải lớn hơn hoặc bằng 0")
    private Integer usageLimit;

    @Min(value = 0, message = "Số lần đã sử dụng phải lớn hơn hoặc bằng 0")
    private Integer usedCount;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean active;
}
