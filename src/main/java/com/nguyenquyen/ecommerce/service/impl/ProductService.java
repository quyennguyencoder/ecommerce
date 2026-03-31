package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.ProductCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.ProductUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductResponse;
import com.nguyenquyen.ecommerce.mapper.ProductMapper;
import com.nguyenquyen.ecommerce.model.Category;
import com.nguyenquyen.ecommerce.model.Product;
import com.nguyenquyen.ecommerce.model.ProductImage;
import com.nguyenquyen.ecommerce.repository.CategoryRepository;
import com.nguyenquyen.ecommerce.repository.ProductImageRepository;
import com.nguyenquyen.ecommerce.repository.ProductRepository;
import com.nguyenquyen.ecommerce.service.IFileService;
import com.nguyenquyen.ecommerce.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;
    private final IFileService fileService;

    @Override
    public List<ProductResponse> getAllProducts(String keyword, Long categoryId, Boolean active, Pageable pageable) {
        return productRepository.findAllByFilters(keyword, categoryId, active, pageable)
                .getContent()
                .stream()
                .map(productMapper::productToProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        return productMapper.productToProductResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isHot(request.getIsHot() != null && request.getIsHot())
                .basePrice(request.getBasePrice() != null ? request.getBasePrice() : BigDecimal.ZERO)
                .active(request.getActive() != null && request.getActive())
                .totalStock(0)
                .category(category)
                .soldQuantity(0)
                .rating(java.math.BigDecimal.ZERO)
                .ratingCount(0)
                .build();

        Product savedProduct = productRepository.save(product);
        return productMapper.productToProductResponse(savedProduct);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getBasePrice() != null) {
            product.setBasePrice(request.getBasePrice());
        }
        if (request.getTotalStock() != null) {
            product.setTotalStock(request.getTotalStock());
        }
        if (request.getIsHot() != null) {
            product.setIsHot(request.getIsHot());
        }
        if (request.getActive() != null) {
            product.setActive(request.getActive());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại"));
            product.setCategory(category);
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.productToProductResponse(updatedProduct);
    }

    @Override
    public ProductResponse updateProductThumbnail(Long id, MultipartFile thumbnail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        if (thumbnail != null && !thumbnail.isEmpty()) {
            String fileName = fileService.uploadProductImage(thumbnail);
            product.setThumbnail(fileName);

            // Lưu thumbnail vào ProductImage
            ProductImage productImage = ProductImage.builder()
                    .imageUrl(fileName)
                    .product(product)
                    .build();
            productImageRepository.save(productImage);
        }

        Product updatedProduct = productRepository.save(product);
        return productMapper.productToProductResponse(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
        productRepository.delete(product);
    }

    @Override
    public List<ProductResponse> getHotProducts(Pageable pageable) {
        return productRepository.findHotProducts(pageable)
                .getContent()
                .stream()
                .map(productMapper::productToProductResponse)
                .toList();
    }

    @Override
    public Resource getThumbnailFile(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        return fileService.loadProductImage(product.getThumbnail());
    }

    @Override
    public Resource getProductImage(String imageName) {
        return fileService.loadProductImage(imageName);
    }
}
