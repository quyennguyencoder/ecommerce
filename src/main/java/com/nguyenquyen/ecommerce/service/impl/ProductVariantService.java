package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.ProductVariantCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.ProductVariantUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductVariantResponse;
import com.nguyenquyen.ecommerce.mapper.ProductVariantMapper;
import com.nguyenquyen.ecommerce.model.AttributeValue;
import com.nguyenquyen.ecommerce.model.Product;
import com.nguyenquyen.ecommerce.model.ProductImage;
import com.nguyenquyen.ecommerce.model.ProductVariant;
import com.nguyenquyen.ecommerce.repository.AttributeValueRepository;
import com.nguyenquyen.ecommerce.repository.ProductImageRepository;
import com.nguyenquyen.ecommerce.repository.ProductRepository;
import com.nguyenquyen.ecommerce.repository.ProductVariantRepository;
import com.nguyenquyen.ecommerce.service.IFileService;
import com.nguyenquyen.ecommerce.service.IProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductVariantService implements IProductVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;
    private final AttributeValueRepository attributeValueRepository;
    private final ProductVariantMapper productVariantMapper;
    private final IFileService fileService;
    private final ProductImageRepository productImageRepository;


    @Override
    public ProductVariantResponse getProductVariantById(Long id) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biến thể sản phẩm không tồn tại với id: " + id));
        return productVariantMapper.productVariantToProductVariantResponse(productVariant);
    }

    @Override
    public List<ProductVariantResponse> getProductVariantsByProductId(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Sản phẩm không tồn tại với id: " + productId);
        }
        List<ProductVariant> productVariants = productVariantRepository.findByProductId(productId);
        return productVariants.stream()
                .map(productVariantMapper::productVariantToProductVariantResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProductVariantResponse getProductVariantBySku(String sku) {
        ProductVariant productVariant = productVariantRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Biến thể sản phẩm không tồn tại với SKU: " + sku));
        return productVariantMapper.productVariantToProductVariantResponse(productVariant);
    }

    @Override
    public ProductVariantResponse createProductVariant(ProductVariantCreateRequest request) {
        // Check if SKU already exists
        if (productVariantRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU đã tồn tại: " + request.getSku());
        }

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + request.getProductId()));

        ProductVariant productVariant = ProductVariant.builder()
                .sku(request.getSku())
                .price(request.getPrice())
                .stock(request.getStock())
                .product(product)
                .build();

        // Add attribute values if provided
        if (request.getAttributeValueIds() != null ) {
            List<AttributeValue> attributeValues = attributeValueRepository.findAllById(request.getAttributeValueIds());
            productVariant.setAttributeValues(attributeValues);
        }
        product.setTotalStock(product.getTotalStock() + request.getStock()); // Cập nhật tổng stock của sản phẩm



        ProductVariant savedProductVariant = productVariantRepository.save(productVariant);
        return productVariantMapper.productVariantToProductVariantResponse(savedProductVariant);
    }

    @Override
    public ProductVariantResponse updateProductVariant(Long id, ProductVariantUpdateRequest request) {
        ProductVariant existingProductVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biến thể sản phẩm không tồn tại với id: " + id));

        // Update SKU if provided and if it's different from current SKU
        if (request.getSku() != null && !request.getSku().isEmpty() && !request.getSku().equals(existingProductVariant.getSku())) {
            if (productVariantRepository.existsBySku(request.getSku())) {
                throw new RuntimeException("SKU đã tồn tại: " + request.getSku());
            }
            existingProductVariant.setSku(request.getSku());
        }

        if (request.getPrice() != null) {
            existingProductVariant.setPrice(request.getPrice());
        }

        if (request.getStock() != null) {
            existingProductVariant.setStock(request.getStock());
        }

        if (request.getProductId() != null) {
            Product product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với id: " + request.getProductId()));
            existingProductVariant.setProduct(product);
        }

        if (request.getAttributeValueIds() != null && !request.getAttributeValueIds().isEmpty()) {
            List<AttributeValue> attributeValues = attributeValueRepository.findAllById(request.getAttributeValueIds());
            existingProductVariant.setAttributeValues(attributeValues);
        }

        ProductVariant updatedProductVariant = productVariantRepository.save(existingProductVariant);
        return productVariantMapper.productVariantToProductVariantResponse(updatedProductVariant);
    }

    @Override
    public void deleteProductVariant(Long id) {
        if (!productVariantRepository.existsById(id)) {
            throw new RuntimeException("Biến thể sản phẩm không tồn tại với id: " + id);
        }
        productVariantRepository.deleteById(id);
    }

    @Override
    public ProductVariantResponse uploadProductVariantImage(Long id, MultipartFile image) {
        ProductVariant productVariant = productVariantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Biến thể sản phẩm không tồn tại với id: " + id));

        if (image != null && !image.isEmpty()) {
            String imageName = fileService.uploadFile(image);
            productVariant.setImage(imageName);
            productVariantRepository.save(productVariant);

            // Lưu image vào ProductImage
            ProductImage productImage = ProductImage.builder()
                    .imageUrl(imageName)
                    .product(productVariant.getProduct())
                    .build();
            productImageRepository.save(productImage);

        }

        return productVariantMapper.productVariantToProductVariantResponse(productVariant);
    }
}
