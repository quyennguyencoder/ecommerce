package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.CategoryResponse;
import com.nguyenquyen.ecommerce.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "productCount", expression = "java(category.getProducts() != null ? category.getProducts().size() : 0)")
    CategoryResponse categoryToCategoryResponse(Category category);
}

