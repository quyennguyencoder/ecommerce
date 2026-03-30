package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.CreateProductVariantRequest;
import com.nguyenquyen.ecommerce.dto.request.UpdateProductVariantRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductVariantResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductVariantService {
    List<ProductVariantResponse> getAllProductVariants(Pageable pageable);
    ProductVariantResponse getProductVariantById(Long id);
    List<ProductVariantResponse> getProductVariantsByProductId(Long productId);
    ProductVariantResponse getProductVariantBySku(String sku);
    ProductVariantResponse createProductVariant(CreateProductVariantRequest request);
    ProductVariantResponse updateProductVariant(Long id, UpdateProductVariantRequest request);
    void deleteProductVariant(Long id);
    ProductVariantResponse uploadProductVariantImage(Long id, MultipartFile image);
    Resource getProductVariantImage(Long id);
}
