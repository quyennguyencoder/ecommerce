package com.nguyenquyen.ecommerce.dto.request.feedback;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackCreateRequest {

    @NotNull(message = "Số sao không được để trống")
    @Min(value = 1, message = "Số sao phải từ 1")
    @Max(value = 5, message = "Số sao không được vượt quá 5")
    private Integer star;

    private String content;

    @NotNull(message = "Product ID không được để trống")
    private Long productId;
}
