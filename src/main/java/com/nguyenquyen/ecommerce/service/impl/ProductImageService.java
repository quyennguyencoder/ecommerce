package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.productImage.CreateProductImageRequest;
import com.nguyenquyen.ecommerce.dto.request.productImage.UpdateProductImageRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductImageResponse;
import com.nguyenquyen.ecommerce.mapper.ProductImageMapper;
import com.nguyenquyen.ecommerce.model.Product;
import com.nguyenquyen.ecommerce.model.ProductImage;
import com.nguyenquyen.ecommerce.repository.ProductImageRepository;
import com.nguyenquyen.ecommerce.repository.ProductRepository;
import com.nguyenquyen.ecommerce.service.IFileService;
import com.nguyenquyen.ecommerce.service.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageService implements IProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;
    private final ProductImageMapper productImageMapper;
    private final IFileService fileService;

    @Override
    public List<ProductImageResponse> getAllProductImages() {
        List<ProductImage> productImages = productImageRepository.findAll();
        return productImages.stream()
                .map(productImageMapper::productImageToProductImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductImageResponse getProductImageById(Long id) {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hình ảnh sản phẩm không tồn tại với id: " + id));
        return productImageMapper.productImageToProductImageResponse(productImage);
    }

    @Override
    public List<ProductImageResponse> getProductImagesByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Sản phẩm không tồn tại với id: " + productId);
        }
        List<ProductImage> productImages = productImageRepository.findByProductId(productId);
        return productImages.stream()
                .map(productImageMapper::productImageToProductImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductImageResponse createProductImage(CreateProductImageRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + request.getProductId()));

        ProductImage productImage = ProductImage.builder()
                .imageUrl(request.getImageUrl())
                .product(product)
                .build();

        ProductImage savedProductImage = productImageRepository.save(productImage);
        return productImageMapper.productImageToProductImageResponse(savedProductImage);
    }

    @Override
    public List<ProductImageResponse> createProductImages(Long productId, List<MultipartFile> images) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + productId));

        return images.stream()
                .filter(image -> !image.isEmpty())
                .map(image -> {
                    String imageUrl = fileService.uploadProductImage(image);
                    ProductImage productImage = ProductImage.builder()
                            .imageUrl(imageUrl)
                            .product(product)
                            .build();
                    return productImageRepository.save(productImage);
                })
                .map(productImageMapper::productImageToProductImageResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductImageResponse updateProductImage(Long id, MultipartFile image) {
        ProductImage existingProductImage = productImageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hình ảnh sản phẩm không tồn tại với id: " + id));

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileService.uploadProductImage(image);
            existingProductImage.setImageUrl(imageUrl);
            productImageRepository.save(existingProductImage);
        }

        return productImageMapper.productImageToProductImageResponse(existingProductImage);
    }

    @Override
    public void deleteProductImage(Long id) {
        if (!productImageRepository.existsById(id)) {
            throw new RuntimeException("Hình ảnh sản phẩm không tồn tại với id: " + id);
        }
        productImageRepository.deleteById(id);
    }
}
