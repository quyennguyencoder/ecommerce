package com.nguyenquyen.ecommerce.dto.request.product;

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
public class ProductCreateRequest {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 255, message = "Tên sản phẩm không được vượt quá 255 ký tự")
    private String name;

    private String description;

    private String thumbnail;

    @DecimalMin(value = "0.0", message = "Đánh giá phải từ 0.0")
    @DecimalMax(value = "5.0", message = "Đánh giá không được vượt quá 5.0")
    private BigDecimal rating;

    @Min(value = 0, message = "Tổng số lượng trong kho phải lớn hơn hoặc bằng 0")
    private Integer totalStock;

    @Size(max = 50, message = "Trạng thái không được vượt quá 50 ký tự")
    private String status;

    @NotNull(message = "Category ID không được để trống")
    private Long categoryId;

    private Boolean isHot;
}
