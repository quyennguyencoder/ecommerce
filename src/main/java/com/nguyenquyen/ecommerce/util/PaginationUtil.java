package com.nguyenquyen.ecommerce.util;

import com.nguyenquyen.ecommerce.dto.PaginationResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class PaginationUtil {
    public static <T> PaginationResponse<T> toPaginationResponse(Page<T> page) {
        return PaginationResponse.<T>builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .content(page.getContent())
                .build();
    }
}
