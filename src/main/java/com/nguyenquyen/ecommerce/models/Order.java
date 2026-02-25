package com.nguyenquyen.ecommerce.models;

import com.nguyenquyen.ecommerce.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Table(name = "orders")
@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String note;
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Float totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private LocalDate shippingDate;
    private String trackingNumber;
    private String paymentMethod;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
