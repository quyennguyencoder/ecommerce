package com.nguyenquyen.ecommerce.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueUpdateRequest {

    @Size(max = 100, message = "Giá trị thuộc tính không được vượt quá 100 ký tự")
    private String value;

    private Long attributeId;
}
