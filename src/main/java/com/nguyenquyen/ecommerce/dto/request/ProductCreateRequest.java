package com.nguyenquyen.ecommerce.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    @DecimalMin(value = "0.00", message = "Giá cơ bản phải lớn hơn hoặc bằng 0")
    private BigDecimal basePrice;

    private Boolean isHot;

    private Boolean active;

    @NotNull(message = "ID danh mục không được để trống")
    private Long categoryId;
}
