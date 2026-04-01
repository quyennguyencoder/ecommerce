package com.nguyenquyen.ecommerce.service;


import com.nguyenquyen.ecommerce.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface IProductImageService {
    ProductImageResponse getProductImageById(Long id);
    List<ProductImageResponse> getProductImagesByProductId(Long productId);
    List<ProductImageResponse> createProductImages(Long productId, List<MultipartFile> images);
    ProductImageResponse updateProductImage(Long id, MultipartFile image);
    void deleteProductImage(Long id);
}
