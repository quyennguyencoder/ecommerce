package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dtos.ProductDTO;
import com.nguyenquyen.ecommerce.dtos.ProductImageDTO;
import com.nguyenquyen.ecommerce.dtos.response.ProductResponse;
import com.nguyenquyen.ecommerce.exception.DataNotFoundException;
import com.nguyenquyen.ecommerce.exception.InvalidParamException;
import com.nguyenquyen.ecommerce.mapper.ProductMapper;
import com.nguyenquyen.ecommerce.models.Category;
import com.nguyenquyen.ecommerce.models.Product;
import com.nguyenquyen.ecommerce.models.ProductImage;
import com.nguyenquyen.ecommerce.repository.CategoryRepository;
import com.nguyenquyen.ecommerce.repository.ProductImageRepository;
import com.nguyenquyen.ecommerce.repository.ProductRepository;
import com.nguyenquyen.ecommerce.service.intf.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper productMapper;

    @Override
    public Product createProduct(ProductDTO productDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("Category not found"));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .price(productDTO.getPrice())
                .description(productDTO.getDescription())
                .thumbnail(productDTO.getThumbnail())
                .category(existingCategory)
                .build();
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws DataNotFoundException {
        return productRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException("Product not found"));
    }

    @Override
    public Page<ProductResponse> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest)
                .map(product -> {
                    ProductResponse productResponse = productMapper.productToProductResponse(product);
                    productResponse.setCreatedAt(product.getCreatedAt());
                    productResponse.setUpdatedAt(product.getUpdatedAt());
                    return productResponse;
                });
    }

    @Override
    public Product updateProduct(Long id, ProductDTO productDTO) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        Category existingCategory = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(()-> new DataNotFoundException("Category not found"));

        existingProduct.setName(productDTO.getName());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(existingCategory);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) throws DataNotFoundException {
        Product existingProduct = getProductById(id);
        productRepository.delete(existingProduct);
    }

    @Override
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }

    @Override
    public ProductImage createProductImage(ProductImageDTO productImageDTO) {
        Product existingProduct = getProductById(productImageDTO.getProductId());
        ProductImage newProductImage = ProductImage.builder()
                .imageUrl(productImageDTO.getImageUrl())
                .product(existingProduct)
                .build();
        int size = productImageRepository.findByProductId(existingProduct.getId()).size();
        if(size > ProductImage.MAXIMUM_IMAGE_PER_PRODUCT){
            throw new InvalidParamException("Maximum images per product is " + ProductImage.MAXIMUM_IMAGE_PER_PRODUCT);
        }
        return productImageRepository.save(newProductImage);
    }
}
