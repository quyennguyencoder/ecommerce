package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
