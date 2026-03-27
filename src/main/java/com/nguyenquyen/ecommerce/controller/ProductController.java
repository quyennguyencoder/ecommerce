package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.product.CreateProductRequest;
import com.nguyenquyen.ecommerce.dto.request.product.UpdateProductRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductResponse;
import com.nguyenquyen.ecommerce.service.IProductService;
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

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<ProductResponse> products = productService.getAllProducts(keyword, categoryId, status, pageable);

        ApiResponse<List<ProductResponse>> response = ApiResponse.<List<ProductResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách sản phẩm thành công")
                .data(products)
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
            @Valid @RequestBody CreateProductRequest request) {

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
            @Valid @RequestBody UpdateProductRequest request) {

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

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<Resource> getProductThumbnail(@PathVariable Long id) {
        try {
            Resource resource = productService.getThumbnailFile(id);
            String mediaType = detectMediaType(resource);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getHotProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<ProductResponse> products = productService.getHotProducts(pageable);

        ApiResponse<List<ProductResponse>> response = ApiResponse.<List<ProductResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách sản phẩm hot thành công")
                .data(products)
                .build();

        return ResponseEntity.ok(response);
    }



    @GetMapping("/images/{imageName}")
    public ResponseEntity<Resource> getProductImage(@PathVariable String imageName) {
        try {
            Resource resource = productService.getProductImage(imageName);
            String mediaType = detectMediaType(resource);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String detectMediaType(Resource resource) {
        try {
            if (resource != null && resource.getFilename() != null) {
                String filename = resource.getFilename().toLowerCase();
                if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")) {
                    return "image/jpeg";
                }
            }
        } catch (Exception e) {
            // Ignore exception, use default
        }
        return "image/png"; // mặc định trả về PNG
    }
}
