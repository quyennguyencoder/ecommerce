package com.nguyenquyen.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRefreshTokenRequest {
    @NotBlank(message = "Refresh token is required")
    private String refreshToken;
}
