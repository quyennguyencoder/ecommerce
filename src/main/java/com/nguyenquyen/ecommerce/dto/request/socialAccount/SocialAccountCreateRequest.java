package com.nguyenquyen.ecommerce.dto.request.socialAccount;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialAccountCreateRequest {

    @NotBlank(message = "Provider ID không được để trống")
    @Size(max = 255, message = "Provider ID không được vượt quá 255 ký tự")
    private String providerId;

    @NotBlank(message = "Provider name không được để trống")
    @Size(max = 50, message = "Provider name không được vượt quá 50 ký tự")
    private String providerName;

    @Email(message = "Email không hợp lệ")
    private String email;
}
