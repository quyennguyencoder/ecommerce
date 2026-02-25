package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dtos.response.ProductResponse;
import com.nguyenquyen.ecommerce.models.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductResponse productToProductResponse(Product product);
}
