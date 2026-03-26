package com.nguyenquyen.ecommerce.dto.request.productVariant;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantUpdateRequest {

    @NotBlank(message = "SKU không được để trống")
    @Size(max = 100, message = "SKU không được vượt quá 100 ký tự")
    private String sku;

    @DecimalMin(value = "0.00", message = "Giá vốn phải lớn hơn hoặc bằng 0")
    private BigDecimal priceCost;

    @NotNull(message = "Giá không được để trống")
    @DecimalMin(value = "0.00", message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    @Min(value = 0, message = "Số lượng trong kho phải lớn hơn hoặc bằng 0")
    private Integer stock;

    private String image;
}
