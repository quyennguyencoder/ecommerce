package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;

import com.nguyenquyen.ecommerce.dto.request.AuthRefreshTokenRequest;
import com.nguyenquyen.ecommerce.dto.request.AuthLoginRequest;
import com.nguyenquyen.ecommerce.dto.response.LoginResponse;
import com.nguyenquyen.ecommerce.dto.response.RefreshTokenResponse;
import com.nguyenquyen.ecommerce.dto.response.AuthUrlResponse;
import com.nguyenquyen.ecommerce.enums.SocialLoginType;
import com.nguyenquyen.ecommerce.service.IAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/auth")
@RequiredArgsConstructor
@Slf4j
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


    @GetMapping("/social-login/{socialLoginType}")
    public ResponseEntity<ApiResponse<AuthUrlResponse>> generateSocialAuthUrl(
            @PathVariable("socialLoginType") SocialLoginType socialLoginType) {
        try {
            log.info("Generating auth URL for social login type: {}", socialLoginType);


            // Generate OAuth URL
            AuthUrlResponse authUrlResponse = authService.generateAuthUrl(socialLoginType);

            ApiResponse<AuthUrlResponse> response = ApiResponse.<AuthUrlResponse>builder()
                    .status(HttpStatus.OK)
                    .message("Lấy OAuth URL thành công")
                    .data(authUrlResponse)
                    .build();

            log.info("Auth URL generated successfully for type: {}", socialLoginType);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid social login type: {}", socialLoginType);
            ApiResponse<AuthUrlResponse> response = ApiResponse.<AuthUrlResponse>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Loại social login không hợp lệ. Hỗ trợ: GOOGLE, FACEBOOK")
                    .build();
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("Error generating auth URL", e);
            ApiResponse<AuthUrlResponse> response = ApiResponse.<AuthUrlResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("Không thể tạo OAuth URL: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }



    @GetMapping("/callback/{socialLoginType}")
    public ResponseEntity<ApiResponse<LoginResponse>> socialLoginCallback(
            @PathVariable("socialLoginType") SocialLoginType socialLoginType,
            @RequestParam("code") String code) {
        try {
            log.info("OAuth callback received for type: {}, code: {}", socialLoginType, code.substring(0, Math.min(10, code.length())) + "...");


            // Call service to handle OAuth callback
            LoginResponse loginResponse = authService.handleSocialLoginCallback(code, socialLoginType);

            ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                    .status(HttpStatus.OK)
                    .message(socialLoginType + " login thành công")
                    .data(loginResponse)
                    .build();

            log.info("OAuth login successful for type: {}", socialLoginType);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Invalid social login type: {}", socialLoginType);
            ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                    .status(HttpStatus.BAD_REQUEST)
                    .message("Loại social login không hợp lệ: " + socialLoginType)
                    .build();
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            log.error("OAuth callback failed for type: {}", socialLoginType, e);
            ApiResponse<LoginResponse> response = ApiResponse.<LoginResponse>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("OAuth callback failed: " + e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
