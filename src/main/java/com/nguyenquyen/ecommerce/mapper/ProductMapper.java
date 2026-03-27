package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.ProductResponse;
import com.nguyenquyen.ecommerce.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponse productToProductResponse(Product product);
}
