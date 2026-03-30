package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.CreateProductRequest;
import com.nguyenquyen.ecommerce.dto.request.UpdateProductRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductResponse;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    List<ProductResponse> getAllProducts(String keyword, Long categoryId, Boolean active, Pageable pageable);
    ProductResponse getProductById(Long id);
    ProductResponse createProduct(CreateProductRequest request);
    ProductResponse updateProduct(Long id, UpdateProductRequest request);
    ProductResponse updateProductThumbnail(Long id, MultipartFile thumbnail);
    void deleteProduct(Long id);
    List<ProductResponse> getHotProducts(Pageable pageable);
    Resource getThumbnailFile(Long id);
    Resource getProductImage(String imageName);
}


