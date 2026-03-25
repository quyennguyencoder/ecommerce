package com.nguyenquyen.ecommerce.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Mã coupon không được để trống")
    @Size(max = 50, message = "Mã coupon không được vượt quá 50 ký tự")
    @Column(name = "code", length = 50, unique = true, nullable = false)
    private String code;

    @NotBlank(message = "Loại giảm giá không được để trống")
    @Size(max = 50, message = "Loại giảm giá không được vượt quá 50 ký tự")
    @Column(name = "discount_type", length = 50, nullable = false)
    private String discountType; // PERCENTAGE, FIXED_AMOUNT

    @NotNull(message = "Giá trị giảm giá không được để trống")
    @DecimalMin(value = "0.00", message = "Giá trị giảm giá phải lớn hơn hoặc bằng 0")
    @Column(name = "discount_value", precision = 12, scale = 2, nullable = false)
    private BigDecimal discountValue;

    @Min(value = 0, message = "Giá trị đơn hàng tối thiểu phải lớn hơn hoặc bằng 0")
    @Column(name = "min_order_value")
    private Integer minOrderValue;

    @Min(value = 0, message = "Giảm giá tối đa phải lớn hơn hoặc bằng 0")
    @Column(name = "max_discount")
    private Integer maxDiscount;

    @Min(value = 0, message = "Giới hạn sử dụng phải lớn hơn hoặc bằng 0")
    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Min(value = 0, message = "Số lần đã sử dụng phải lớn hơn hoặc bằng 0")
    @Column(name = "used_count")
    private Integer usedCount;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Size(max = 50, message = "Trạng thái không được vượt quá 50 ký tự")
    @Column(name = "status", length = 50)
    private String status;

    @OneToMany(mappedBy = "coupon")
    @Builder.Default
    private Set<Order> orders = new HashSet<>();
}