package com.nguyenquyen.ecommerce.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String phone;
    private String name;
    private LocalDate dob;
    private String address;
    private String avatar;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private RoleResponse role;
}
