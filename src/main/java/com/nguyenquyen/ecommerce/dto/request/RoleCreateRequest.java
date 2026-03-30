package com.nguyenquyen.ecommerce.dto.request;

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
public class RoleCreateRequest {

    @NotBlank(message = "Tên role không được để trống")
    @Size(max = 50, message = "Tên role không được vượt quá 50 ký tự")
    private String name;
}
