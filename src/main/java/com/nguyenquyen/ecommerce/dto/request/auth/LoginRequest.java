package com.nguyenquyen.ecommerce.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "Email hoặc số điện thoại không được để trống")
    private String username; // email hoặc phone

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;
}
