package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
