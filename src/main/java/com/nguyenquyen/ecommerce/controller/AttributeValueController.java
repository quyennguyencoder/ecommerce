package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.AttributeValueCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.AttributeValueUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.AttributeValueResponse;
import com.nguyenquyen.ecommerce.service.IAttributeValueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/attribute-values")
@RequiredArgsConstructor
public class AttributeValueController {

    private final IAttributeValueService attributeValueService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttributeValueResponse>>> getAllAttributeValues() {
        List<AttributeValueResponse> attributeValues = attributeValueService.getAllAttributeValues();

        ApiResponse<List<AttributeValueResponse>> response = ApiResponse.<List<AttributeValueResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách giá trị thuộc tính thành công")
                .data(attributeValues)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttributeValueResponse>> getAttributeValueById(@PathVariable Long id) {
        AttributeValueResponse attributeValue = attributeValueService.getAttributeValueById(id);

        ApiResponse<AttributeValueResponse> response = ApiResponse.<AttributeValueResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin giá trị thuộc tính thành công")
                .data(attributeValue)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/attribute/{attributeId}")
    public ResponseEntity<ApiResponse<List<AttributeValueResponse>>> getAttributeValuesByAttributeId(
            @PathVariable Long attributeId) {
        List<AttributeValueResponse> attributeValues = attributeValueService.getAttributeValuesByAttributeId(attributeId);

        ApiResponse<List<AttributeValueResponse>> response = ApiResponse.<List<AttributeValueResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách giá trị theo thuộc tính thành công")
                .data(attributeValues)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttributeValueResponse>> createAttributeValue(
            @Valid @RequestBody AttributeValueCreateRequest request) {

        AttributeValueResponse attributeValue = attributeValueService.createAttributeValue(request);

        ApiResponse<AttributeValueResponse> response = ApiResponse.<AttributeValueResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo giá trị thuộc tính thành công")
                .data(attributeValue)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttributeValueResponse>> updateAttributeValue(
            @PathVariable Long id,
            @Valid @RequestBody AttributeValueUpdateRequest request) {

        AttributeValueResponse attributeValue = attributeValueService.updateAttributeValue(id, request);

        ApiResponse<AttributeValueResponse> response = ApiResponse.<AttributeValueResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật giá trị thuộc tính thành công")
                .data(attributeValue)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttributeValue(@PathVariable Long id) {
        attributeValueService.deleteAttributeValue(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa giá trị thuộc tính thành công")
                .build();

        return ResponseEntity.ok(response);
    }
}
