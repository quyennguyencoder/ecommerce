package com.nguyenquyen.ecommerce.model;


import com.nguyenquyen.ecommerce.enums.Gender;
import com.nguyenquyen.ecommerce.enums.ShippingMethod;
import com.nguyenquyen.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Tên người nhận không được để trống")
    @Size(max = 255, message = "Tên người nhận không được vượt quá 255 ký tự")
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(\\+84|0)[0-9]{9,10}$", message = "Số điện thoại không hợp lệ")
    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @Email(message = "Email không hợp lệ")
    @Column(name = "email", length = 255)
    private String email;

    @NotBlank(message = "Địa chỉ giao hàng không được để trống")
    @Size(max = 255, message = "Địa chỉ giao hàng không được vượt quá 255 ký tự")
    @Column(name = "shipping_address", length = 255, nullable = false)
    private String shippingAddress;

    @Size(max = 255, message = "Ghi chú không được vượt quá 255 ký tự")
    @Column(name = "note", length = 255)
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method", length = 50, nullable = false)
    private ShippingMethod shippingMethod;

    @DecimalMin(value = "0.00", message = "Phí vận chuyển phải lớn hơn hoặc bằng 0")
    @Column(name = "shipping_fee", precision = 12, scale = 2)
    private BigDecimal shippingFee;

    @NotNull(message = "Tổng tiền không được để trống")
    @DecimalMin(value = "0.00", message = "Tổng tiền phải lớn hơn hoặc bằng 0")
    @Column(name = "total", precision = 12, scale = 2, nullable = false)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private OrderStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;
}