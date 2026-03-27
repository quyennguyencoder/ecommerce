package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.category.CreateCategoryRequest;
import com.nguyenquyen.ecommerce.dto.request.category.UpdateCategoryRequest;
import com.nguyenquyen.ecommerce.dto.response.CategoryResponse;
import com.nguyenquyen.ecommerce.mapper.CategoryMapper;
import com.nguyenquyen.ecommerce.model.Category;
import com.nguyenquyen.ecommerce.repository.CategoryRepository;
import com.nguyenquyen.ecommerce.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::categoryToCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với id: " + id));
        return categoryMapper.categoryToCategoryResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        Category category = Category.builder()
                .name(request.getName())
                .build();

        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.categoryToCategoryResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với id: " + id));

        if (request.getName() != null && !request.getName().isEmpty()) {
            existingCategory.setName(request.getName());
        }

        Category updatedCategory = categoryRepository.save(existingCategory);
        return categoryMapper.categoryToCategoryResponse(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Danh mục không tồn tại với id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}

