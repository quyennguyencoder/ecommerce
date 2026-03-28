package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE (:status IS NULL OR o.status = :status)")
    Page<Order> findAllOrdersWithStatus(@Param("status") String status, Pageable pageable);
}
