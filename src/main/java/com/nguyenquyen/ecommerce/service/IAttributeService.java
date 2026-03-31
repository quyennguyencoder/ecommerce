package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.AttributeCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.AttributeUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.AttributeResponse;

import java.util.List;

public interface IAttributeService {
    List<AttributeResponse> getAllAttributes();
    AttributeResponse getAttributeById(Long id);
    AttributeResponse createAttribute(AttributeCreateRequest request);
    AttributeResponse updateAttribute(Long id, AttributeUpdateRequest request);
    void deleteAttribute(Long id);
}


