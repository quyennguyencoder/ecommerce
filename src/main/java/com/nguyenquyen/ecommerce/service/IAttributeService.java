package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.CreateAttributeRequest;
import com.nguyenquyen.ecommerce.dto.request.UpdateAttributeRequest;
import com.nguyenquyen.ecommerce.dto.response.AttributeResponse;

import java.util.List;

public interface IAttributeService {
    List<AttributeResponse> getAllAttributes();
    AttributeResponse getAttributeById(Long id);
    AttributeResponse createAttribute(CreateAttributeRequest request);
    AttributeResponse updateAttribute(Long id, UpdateAttributeRequest request);
    void deleteAttribute(Long id);
}


