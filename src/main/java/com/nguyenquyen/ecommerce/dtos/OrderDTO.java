package com.nguyenquyen.ecommerce.dtos;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDTO {
    @Min(value = 1, message = "User ID must be minimum 1")
    private Long userId;
    private String fullName;
    private String email;
    @NotBlank(message = "Phone number is required")
    @Size(min = 10, max = 15, message = "Phone number must be between 10 and 15 characters")
    private String phoneNumber;
    @NotNull(message = "Address is required")
    private String address;
    private String note;
    @Min(value = 0, message = "Total money must be minimum 0")
    private Float totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private LocalDate shippingDate;
    private String paymentMethod;

}
