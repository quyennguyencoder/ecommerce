package com.nguyenquyen.ecommerce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeResponse {

    private Long id;
    private String name;
    private Integer attributeValueCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
