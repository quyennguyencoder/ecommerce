package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
