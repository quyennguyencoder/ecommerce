package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.response.CategoryResponse;
import com.nguyenquyen.ecommerce.service.ICategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class CategoryControllerTest {

    @Mock
    private ICategoryService categoryService;

    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        try (AutoCloseable ignored = MockitoAnnotations.openMocks(this)) {
            categoryController = new CategoryController(categoryService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllCategories() {
        // Arrange: Tạo dữ liệu mock
        List<CategoryResponse> categories = new ArrayList<>();
        categories.add(CategoryResponse.builder()
                .id(1L)
                .name("Electronics")
                .build());
        categories.add(CategoryResponse.builder()
                .id(2L)
                .name("Clothing")
                .build());

        when(categoryService.getAllCategories()).thenReturn(categories);

        // Act: Gọi phương thức controller
        var response = categoryController.getAllCategories();

        // Assert: Kiểm tra kết quả
        assertNotNull(response);

        var responseBody = response.getBody();
        assertNotNull(responseBody);

        var data = responseBody.getData();
        assertNotNull(data);
        assertEquals(2, data.size());
        assertEquals("Electronics", data.get(0).getName());
        assertEquals("Clothing", data.get(1).getName());
        assertEquals("Lấy danh sách danh mục thành công", responseBody.getMessage());

        // Verify: Kiểm tra rằng method được gọi đúng 1 lần
        verify(categoryService).getAllCategories();
    }

}
