package com.nguyenquyen.ecommerce.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String fullName;
    @NotBlank(message = "Username must not be blank")
    private String phoneNumber;
    private String address;

    @NotBlank(message = "Password must not be blank")
    private String password;
    private String retypePassword;
    private Date dateOfBirth;
    private int facebookAccountId;
    private int googleAccountId;
//    @NotNull(message = "Role ID is required")
    private Long roleId;
}
