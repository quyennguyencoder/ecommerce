package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.AttributeValueCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.AttributeValueUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.AttributeValueResponse;

import java.util.List;

public interface IAttributeValueService {
    List<AttributeValueResponse> getAllAttributeValues();
    AttributeValueResponse getAttributeValueById(Long id);
    List<AttributeValueResponse> getAttributeValuesByAttributeId(Long attributeId);
    AttributeValueResponse createAttributeValue(AttributeValueCreateRequest request);
    AttributeValueResponse updateAttributeValue(Long id, AttributeValueUpdateRequest request);
    void deleteAttributeValue(Long id);
}


