package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import com.nguyenquyen.ecommerce.dto.request.ProductCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.ProductUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductResponse;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    PaginationResponse<ProductResponse> getAllProducts(String keyword, Long categoryId, Boolean active, int page, int size);
    ProductResponse getProductById(Long id);
    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse updateProduct(Long id, ProductUpdateRequest request);
    ProductResponse updateProductThumbnail(Long id, MultipartFile thumbnail);
    void deleteProduct(Long id);
    Page<ProductResponse> getHotProducts(int page, int size);
}


