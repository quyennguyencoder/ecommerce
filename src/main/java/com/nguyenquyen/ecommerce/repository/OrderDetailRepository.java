package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
