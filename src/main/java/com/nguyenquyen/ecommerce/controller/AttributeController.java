package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.attribute.CreateAttributeRequest;
import com.nguyenquyen.ecommerce.dto.request.attribute.UpdateAttributeRequest;
import com.nguyenquyen.ecommerce.dto.response.AttributeResponse;
import com.nguyenquyen.ecommerce.service.IAttributeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final IAttributeService attributeService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AttributeResponse>>> getAllAttributes() {
        List<AttributeResponse> attributes = attributeService.getAllAttributes();

        ApiResponse<List<AttributeResponse>> response = ApiResponse.<List<AttributeResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách thuộc tính thành công")
                .data(attributes)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AttributeResponse>> getAttributeById(@PathVariable Long id) {
        AttributeResponse attribute = attributeService.getAttributeById(id);

        ApiResponse<AttributeResponse> response = ApiResponse.<AttributeResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin thuộc tính thành công")
                .data(attribute)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AttributeResponse>> createAttribute(
            @Valid @RequestBody CreateAttributeRequest request) {

        AttributeResponse attribute = attributeService.createAttribute(request);

        ApiResponse<AttributeResponse> response = ApiResponse.<AttributeResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo thuộc tính thành công")
                .data(attribute)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AttributeResponse>> updateAttribute(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAttributeRequest request) {

        AttributeResponse attribute = attributeService.updateAttribute(id, request);

        ApiResponse<AttributeResponse> response = ApiResponse.<AttributeResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật thuộc tính thành công")
                .data(attribute)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteAttribute(@PathVariable Long id) {
        attributeService.deleteAttribute(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa thuộc tính thành công")
                .build();

        return ResponseEntity.ok(response);
    }
}
