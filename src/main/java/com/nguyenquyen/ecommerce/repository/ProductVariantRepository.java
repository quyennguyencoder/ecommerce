package com.nguyenquyen.ecommerce.repository;

import com.nguyenquyen.ecommerce.model.ProductVariant;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
    List<ProductVariant> findByProductId(Long productId);

    Optional<ProductVariant> findBySku(String sku);

    boolean existsBySku(@NotBlank(message = "SKU không được để trống") @Size(max = 100, message = "SKU không được vượt quá 100 ký tự") String sku);
}
