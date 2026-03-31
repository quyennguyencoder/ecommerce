package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.ProductVariantCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.ProductVarianUpdatetRequest;
import com.nguyenquyen.ecommerce.dto.response.ProductVariantResponse;
import com.nguyenquyen.ecommerce.service.IProductVariantService;
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
@RequestMapping("${api.prefix}/product-variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final IProductVariantService productVariantService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> getAllProductVariants(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        List<ProductVariantResponse> productVariants = productVariantService.getAllProductVariants(pageable);

        ApiResponse<List<ProductVariantResponse>> response = ApiResponse.<List<ProductVariantResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách biến thể sản phẩm thành công")
                .data(productVariants)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> getProductVariantById(@PathVariable Long id) {
        ProductVariantResponse productVariant = productVariantService.getProductVariantById(id);

        ApiResponse<ProductVariantResponse> response = ApiResponse.<ProductVariantResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin biến thể sản phẩm thành công")
                .data(productVariant)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ProductVariantResponse>>> getProductVariantsByProductId(
            @PathVariable Long productId) {

        List<ProductVariantResponse> productVariants = productVariantService.getProductVariantsByProductId(productId);

        ApiResponse<List<ProductVariantResponse>> response = ApiResponse.<List<ProductVariantResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách biến thể theo sản phẩm thành công")
                .data(productVariants)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> getProductVariantBySku(@PathVariable String sku) {
        ProductVariantResponse productVariant = productVariantService.getProductVariantBySku(sku);

        ApiResponse<ProductVariantResponse> response = ApiResponse.<ProductVariantResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy biến thể sản phẩm theo SKU thành công")
                .data(productVariant)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductVariantResponse>> createProductVariant(
            @Valid @RequestBody ProductVariantCreateRequest request) {

        ProductVariantResponse productVariant = productVariantService.createProductVariant(request);

        ApiResponse<ProductVariantResponse> response = ApiResponse.<ProductVariantResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo biến thể sản phẩm thành công")
                .data(productVariant)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> updateProductVariant(
            @PathVariable Long id,
            @Valid @RequestBody ProductVarianUpdatetRequest request) {

        ProductVariantResponse productVariant = productVariantService.updateProductVariant(id, request);

        ApiResponse<ProductVariantResponse> response = ApiResponse.<ProductVariantResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật biến thể sản phẩm thành công")
                .data(productVariant)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductVariant(@PathVariable Long id) {
        productVariantService.deleteProductVariant(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa biến thể sản phẩm thành công")
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<ApiResponse<ProductVariantResponse>> uploadProductVariantImage(
            @PathVariable Long id,
            @RequestParam("image") MultipartFile image) {

        ProductVariantResponse productVariant = productVariantService.uploadProductVariantImage(id, image);

        ApiResponse<ProductVariantResponse> response = ApiResponse.<ProductVariantResponse>builder()
                .status(HttpStatus.OK)
                .message("Upload hình ảnh biến thể sản phẩm thành công")
                .data(productVariant)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<Resource> getProductVariantImage(@PathVariable Long id) {
        try {
            Resource resource = productVariantService.getProductVariantImage(id);
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

