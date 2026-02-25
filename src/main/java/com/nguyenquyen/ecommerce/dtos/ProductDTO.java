package com.nguyenquyen.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDTO {
    @NotBlank(message = "Product name must not be blank")
    @Size(min= 3, max=200, message = "Product name must be between 3 and 100 characters")
    private String name;

    @Min(value = 0, message = "Price must be minimum 0")
    @Max(value = 100000000, message = "Price must be maximum 10 000 000")
    private Float price;
    private String thumbnail;
    private String description;
    private Long categoryId;
}
