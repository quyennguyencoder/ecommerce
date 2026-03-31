package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.CategoryCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.CategoryUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CategoryCreateRequest request);
    CategoryResponse updateCategory(Long id, CategoryUpdateRequest request);
    void deleteCategory(Long id);
}


