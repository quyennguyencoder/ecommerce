package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
