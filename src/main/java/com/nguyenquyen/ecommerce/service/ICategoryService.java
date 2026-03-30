package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.CreateCategoryRequest;
import com.nguyenquyen.ecommerce.dto.request.UpdateCategoryRequest;
import com.nguyenquyen.ecommerce.dto.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long id);
    CategoryResponse createCategory(CreateCategoryRequest request);
    CategoryResponse updateCategory(Long id, UpdateCategoryRequest request);
    void deleteCategory(Long id);
}


