package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;

import com.nguyenquyen.ecommerce.dto.request.AuthRefreshTokenRequest;
import com.nguyenquyen.ecommerce.dto.request.AuthLoginRequest;
import com.nguyenquyen.ecommerce.dto.response.LoginResponse;
import com.nguyenquyen.ecommerce.dto.response.RefreshTokenResponse;
import com.nguyenquyen.ecommerce.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody AuthLoginRequest request) {
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

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestHeader("Authorization") String authHeader) {
        try {
            // Extract token from Authorization header (Bearer <token>)
            String token = authHeader.replace("Bearer ", "");
            authService.logout(token);

            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(HttpStatus.OK)
                    .message("Đăng xuất thành công")
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<RefreshTokenResponse>> refreshToken(
            @Valid @RequestBody AuthRefreshTokenRequest request) {
        try {
            RefreshTokenResponse refreshTokenResponse = authService.refreshToken(request.getRefreshToken());

            ApiResponse<RefreshTokenResponse> response = ApiResponse.<RefreshTokenResponse>builder()
                    .status(HttpStatus.OK)
                    .message("Làm mới token thành công")
                    .data(refreshTokenResponse)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<RefreshTokenResponse> response = ApiResponse.<RefreshTokenResponse>builder()
                    .status(HttpStatus.UNAUTHORIZED)
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
