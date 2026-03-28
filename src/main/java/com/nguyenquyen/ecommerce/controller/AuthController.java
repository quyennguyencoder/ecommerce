package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.auth.LoginRequest;
import com.nguyenquyen.ecommerce.dto.response.auth.LoginResponse;
import com.nguyenquyen.ecommerce.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse loginResponse = authService.login(request);
            ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                    .status(HttpStatus.OK)
                    .message("Đăng nhập thành công")
                    .data(loginResponse)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
