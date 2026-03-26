package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}
