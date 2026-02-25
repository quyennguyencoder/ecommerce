package com.nguyenquyen.ecommerce.service.intf;

import com.nguyenquyen.ecommerce.dtos.ProductDTO;
import com.nguyenquyen.ecommerce.dtos.ProductImageDTO;
import com.nguyenquyen.ecommerce.dtos.response.ProductResponse;
import com.nguyenquyen.ecommerce.exception.DataNotFoundException;
import com.nguyenquyen.ecommerce.exception.InvalidParamException;
import com.nguyenquyen.ecommerce.models.Product;
import com.nguyenquyen.ecommerce.models.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) ;
    Product getProductById(Long id) ;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(Long id, ProductDTO productDTO) ;
    void deleteProduct(Long id) throws DataNotFoundException;
    boolean existsByName(String name);
    ProductImage createProductImage(ProductImageDTO productImageDTO) ;
}
