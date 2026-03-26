package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.dto.ApiResponse;
import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByEmailRequest;
import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.request.user.ChangePasswordRequest;
import com.nguyenquyen.ecommerce.dto.request.user.UserUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import com.nguyenquyen.ecommerce.service.IFileService;
import com.nguyenquyen.ecommerce.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IFileService fileService;

    @Value("${uploads.avatars}")
    private String avatarUploadPath;

    @PostMapping("/register/email")
    public ResponseEntity<ApiResponse<UserResponse>> registerByEmail(
            @Valid @RequestBody RegisterByEmailRequest request) {
        UserResponse userResponse = userService.registerByEmail(request);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Đăng ký bằng email thành công")
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/phone")
    public ResponseEntity<ApiResponse<UserResponse>> registerByPhone(
            @Valid @RequestBody RegisterByPhoneRequest request) {
        UserResponse userResponse = userService.registerByPhone(request);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.CREATED)
                .message("Đăng ký bằng số điện thoại thành công")
                .data(userResponse)
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse result = userService.getUserById(id);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK)
                .message("Lấy thông tin user thành công")
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        List<UserResponse> result = userService.getAllUsers(keyword, role, pageRequest);
        ApiResponse<List<UserResponse>> response = ApiResponse.<List<UserResponse>>builder()
                .status(HttpStatus.OK)
                .message("Lấy danh sách user thành công")
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse result = userService.updateUser(id, request);
        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật user thành công")
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Xóa user thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{userId}/role/{roleId}")
    public ResponseEntity<ApiResponse<Void>> updateUserRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        userService.updateUserRole(userId, roleId);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Cập nhật role cho user thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<Void>> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Vô hiệu hóa user thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<Void>> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Kích hoạt user thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/avatar/upload")
    public ResponseEntity<ApiResponse<String>> uploadAvatar(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        String fileName = fileService.uploadAvatar(file);
        userService.updateUserAvatar(id, fileName);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status(HttpStatus.OK)
                .message("Upload avatar thành công")
                .data(fileName)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<Resource> getAvatar(@PathVariable Long id) {
        try {
            UserResponse user = userService.getUserById(id);
            Resource resource = fileService.loadAvatarFile(user.getAvatar());
            String mediaType = detectMediaType(user.getAvatar());

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mediaType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + (user.getAvatar() != null ? user.getAvatar() : "default-avatar.png") + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}/change-password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(id, request);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK)
                .message("Đổi mật khẩu thành công")
                .build();
        return ResponseEntity.ok(response);
    }

    private String detectMediaType(String avatarFileName) {
        try {
            if (avatarFileName != null && !avatarFileName.isEmpty()) {
                String mediaType = Files.probeContentType(Paths.get(avatarUploadPath, avatarFileName));
                if (mediaType != null) {
                    return mediaType;
                }
            }
        } catch (Exception e) {
            // Ignore exception, use default
        }
        // return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return "image/png"; // mặc định trả về PNG vì avatar mặc định là PNG
    }

}
