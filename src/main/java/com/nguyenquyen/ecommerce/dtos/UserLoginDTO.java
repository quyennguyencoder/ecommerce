package com.nguyenquyen.ecommerce.dtos;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDTO {
    @NotBlank(message = "Phone number must not be blank")
    private String phoneNumber;
    @NotBlank(message = "Password must not be blank")
    private String password;
}
