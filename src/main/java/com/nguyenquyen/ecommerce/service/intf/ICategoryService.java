package com.nguyenquyen.ecommerce.service.intf;


import com.nguyenquyen.ecommerce.dtos.CategoryDTO;
import com.nguyenquyen.ecommerce.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
