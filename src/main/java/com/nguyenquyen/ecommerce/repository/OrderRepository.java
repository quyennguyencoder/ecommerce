package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
