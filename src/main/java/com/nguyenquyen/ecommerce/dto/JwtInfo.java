package com.nguyenquyen.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtInfo {
    private String id;              // jti (JWT ID) - UUID
    private String subject;         // sub (subject) - email hoặc phone
    private String roles;           // roles - tên role của user
    private Date issuedAt;          // iat (issued at time)
    private Date expiresAt;         // exp (expiration time)
}
