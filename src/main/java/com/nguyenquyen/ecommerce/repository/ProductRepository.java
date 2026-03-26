package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
