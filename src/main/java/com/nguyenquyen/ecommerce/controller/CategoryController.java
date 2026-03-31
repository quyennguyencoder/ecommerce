package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.CategoryCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.CategoryUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.CategoryResponse;
import com.nguyenquyen.ecommerce.service.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> categories = categoryService.getAllCategories();

        ApiResponse<List<CategoryResponse>> response = ApiResponse.<List<CategoryResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách danh mục thành công")
                .data(categories)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse category = categoryService.getCategoryById(id);

        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin danh mục thành công")
                .data(category)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @Valid @RequestBody CategoryCreateRequest request) {

        CategoryResponse category = categoryService.createCategory(request);

        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo danh mục thành công")
                .data(category)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryUpdateRequest request) {

        CategoryResponse category = categoryService.updateCategory(id, request);

        ApiResponse<CategoryResponse> response = ApiResponse.<CategoryResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật danh mục thành công")
                .data(category)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa danh mục thành công")
                .build();

        return ResponseEntity.ok(response);
    }
}


