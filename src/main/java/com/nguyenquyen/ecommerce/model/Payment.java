package com.nguyenquyen.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255, message = "Transaction ID không được vượt quá 255 ký tự")
    @Column(name = "transaction_id", length = 255)
    private String transactionId;

    @Size(max = 50, message = "Phương thức thanh toán không được vượt quá 50 ký tự")
    @Column(name = "payment_method", length = 50)
    private String paymentMethod; // COD, VNPAY, MOMO, BANK_TRANSFER

    @Size(max = 50, message = "Trạng thái thanh toán không được vượt quá 50 ký tự")
    @Column(name = "payment_status", length = 50)
    private String paymentStatus; // PENDING, PAID, FAILED, REFUNDED

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;
}