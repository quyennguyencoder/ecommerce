package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.ProductVariantResponse;
import com.nguyenquyen.ecommerce.model.ProductVariant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductVariantMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "attributeValueIds", expression = "java(productVariant.getAttributeValues() != null ? productVariant.getAttributeValues().stream().map(av -> av.getId()).toList() : java.util.List.of())")
    ProductVariantResponse productVariantToProductVariantResponse(ProductVariant productVariant);
}
