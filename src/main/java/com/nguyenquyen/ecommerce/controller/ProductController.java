package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import com.nguyenquyen.ecommerce.dto.request.ProductCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.ProductUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductResponse;
import com.nguyenquyen.ecommerce.service.IProductService;
import com.nguyenquyen.ecommerce.util.PaginationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        var productPage = productService.getAllProducts(keyword, categoryId, active, pageable);
        PaginationResponse<ProductResponse> paginationResponse = PaginationUtil.toPaginationResponse(productPage);

        ApiResponse<PaginationResponse<ProductResponse>> response = ApiResponse.<PaginationResponse<ProductResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách sản phẩm thành công")
                .data(paginationResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse product = productService.getProductById(id);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin sản phẩm thành công")
                .data(product)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request) {

        ProductResponse product = productService.createProduct(request);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo sản phẩm thành công")
                .data(product)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {

        ProductResponse product = productService.updateProduct(id, request);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật sản phẩm thành công")
                .data(product)
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/thumbnail")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProductThumbnail(
            @PathVariable Long id,
            @RequestParam("thumbnail") MultipartFile thumbnail) {

        ProductResponse product = productService.updateProductThumbnail(id, thumbnail);

        ApiResponse<ProductResponse> response = ApiResponse.<ProductResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật ảnh đại diện sản phẩm thành công")
                .data(product)
                .build();

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa sản phẩm thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/hot")
    public ResponseEntity<ApiResponse<PaginationResponse<ProductResponse>>> getHotProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        var productPage = productService.getHotProducts(pageable);
        PaginationResponse<ProductResponse> paginationResponse = PaginationUtil.toPaginationResponse(productPage);

        ApiResponse<PaginationResponse<ProductResponse>> response = ApiResponse.<PaginationResponse<ProductResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách sản phẩm hot thành công")
                .data(paginationResponse)
                .build();

        return ResponseEntity.ok(response);
    }
}
