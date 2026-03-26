package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
