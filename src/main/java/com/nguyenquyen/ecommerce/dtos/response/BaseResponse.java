package com.nguyenquyen.ecommerce.dtos.response;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseResponse {
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
