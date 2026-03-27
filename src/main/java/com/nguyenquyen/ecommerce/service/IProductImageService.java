package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.productImage.CreateProductImageRequest;
import com.nguyenquyen.ecommerce.dto.request.productImage.UpdateProductImageRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductImageService {
    List<ProductImageResponse> getAllProductImages();
    ProductImageResponse getProductImageById(Long id);
    List<ProductImageResponse> getProductImagesByProductId(Long productId);
    ProductImageResponse createProductImage(CreateProductImageRequest request);
    List<ProductImageResponse> createProductImages(Long productId, List<MultipartFile> images);
    ProductImageResponse updateProductImage(Long id, MultipartFile image);
    void deleteProductImage(Long id);
}
