package com.nguyenquyen.ecommerce.dto.request.attribute;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAttributeRequest {

    @NotBlank(message = "Tên thuộc tính không được để trống")
    @Size(max = 100, message = "Tên thuộc tính không được vượt quá 100 ký tự")
    private String name;
}
