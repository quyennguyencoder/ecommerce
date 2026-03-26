package com.nguyenquyen.ecommerce.dto.request.attributeValue;

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
public class AttributeValueUpdateRequest {

    @NotBlank(message = "Giá trị thuộc tính không được để trống")
    @Size(max = 100, message = "Giá trị thuộc tính không được vượt quá 100 ký tự")
    private String value;
}
