package com.nguyenquyen.ecommerce.dto.request.product;

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
public class UpdateProductRequest {

    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;

    @Size(max = 1000, message = "Mô tả không được vượt quá 1000 ký tự")
    private String description;

    private String thumbnail;

    @DecimalMin(value = "0.00", message = "Giá tối thiểu phải lớn hơn hoặc bằng 0")
    private BigDecimal minPrice;

    @DecimalMin(value = "0.00", message = "Giá tối đa phải lớn hơn hoặc bằng 0")
    private BigDecimal maxPrice;

    @Min(value = 0, message = "Tổng số lượng phải lớn hơn hoặc bằng 0")
    private Integer totalStock;

    private Boolean isHot;

    private String status;

    private Long categoryId;
}
