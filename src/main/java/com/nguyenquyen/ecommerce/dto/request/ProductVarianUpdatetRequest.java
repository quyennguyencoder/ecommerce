package com.nguyenquyen.ecommerce.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVarianUpdatetRequest {

    @Size(max = 100, message = "SKU không được vượt quá 100 ký tự")
    private String sku;

    @DecimalMin(value = "0.00", message = "Giá phải lớn hơn hoặc bằng 0")
    private BigDecimal price;

    @Min(value = 0, message = "Số lượng trong kho phải lớn hơn hoặc bằng 0")
    private Integer stock;

    private Long productId;

    private List<Long> attributeValueIds;
}
