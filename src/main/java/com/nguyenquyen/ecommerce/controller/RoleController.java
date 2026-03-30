package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.RoleCreateRequest;

import com.nguyenquyen.ecommerce.dto.response.RoleResponse;
import com.nguyenquyen.ecommerce.service.IRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/roles")
@RequiredArgsConstructor
public class RoleController {

    private final IRoleService roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleCreateRequest request) {
        RoleResponse roleResponse = roleService.createRole(request);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Tạo role thành công")
                .data(roleResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        List<RoleResponse> result = roleService.getAllRoles();
        ApiResponse<List<RoleResponse>> response = ApiResponse.<List<RoleResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách role thành công")
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

}
