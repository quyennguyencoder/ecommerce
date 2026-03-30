package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.response.ProductImageResponse;
import com.nguyenquyen.ecommerce.service.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/product-images")
@RequiredArgsConstructor
public class ProductImageController {

    private final IProductImageService productImageService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getAllProductImages() {
        List<ProductImageResponse> productImages = productImageService.getAllProductImages();

        ApiResponse<List<ProductImageResponse>> response = ApiResponse.<List<ProductImageResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách hình ảnh sản phẩm thành công")
                .data(productImages)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImageResponse>> getProductImageById(@PathVariable Long id) {
        ProductImageResponse productImage = productImageService.getProductImageById(id);

        ApiResponse<ProductImageResponse> response = ApiResponse.<ProductImageResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin hình ảnh sản phẩm thành công")
                .data(productImage)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getProductImagesByProductId(
            @PathVariable Long productId) {

        List<ProductImageResponse> productImages = productImageService.getProductImagesByProductId(productId);

        ApiResponse<List<ProductImageResponse>> response = ApiResponse.<List<ProductImageResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách hình ảnh theo sản phẩm thành công")
                .data(productImages)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> createProductImage(
            @PathVariable Long productId,
            @RequestParam("images") List<MultipartFile> images) {

        List<ProductImageResponse> productImages = productImageService.createProductImages(productId, images);

        ApiResponse<List<ProductImageResponse>> response = ApiResponse.<List<ProductImageResponse>>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo hình ảnh sản phẩm thành công")
                .data(productImages)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductImageResponse>> updateProductImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image) {

        ProductImageResponse productImage = productImageService.updateProductImage(id, image);

        ApiResponse<ProductImageResponse> response = ApiResponse.<ProductImageResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật hình ảnh sản phẩm thành công")
                .data(productImage)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/image/{imageName}")
    public ResponseEntity<Resource> getProductImage(@PathVariable String imageName) {
        try {
            Resource resource = productImageService.getProductImage(imageName);
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
    public ResponseEntity<ApiResponse<Void>> deleteProductImage(@PathVariable Long id) {
        productImageService.deleteProductImage(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa hình ảnh sản phẩm thành công")
                .build();

        return ResponseEntity.ok(response);
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
