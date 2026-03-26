package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}
