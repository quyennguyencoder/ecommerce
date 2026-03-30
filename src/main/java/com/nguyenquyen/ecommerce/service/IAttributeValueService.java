package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.CreateAttributeValueRequest;
import com.nguyenquyen.ecommerce.dto.request.UpdateAttributeValueRequest;
import com.nguyenquyen.ecommerce.dto.response.AttributeValueResponse;

import java.util.List;

public interface IAttributeValueService {
    List<AttributeValueResponse> getAllAttributeValues();
    AttributeValueResponse getAttributeValueById(Long id);
    List<AttributeValueResponse> getAttributeValuesByAttributeId(Long attributeId);
    AttributeValueResponse createAttributeValue(CreateAttributeValueRequest request);
    AttributeValueResponse updateAttributeValue(Long id, UpdateAttributeValueRequest request);
    void deleteAttributeValue(Long id);
}


