package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.AttributeResponse;
import com.nguyenquyen.ecommerce.model.Attribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttributeMapper {

    @Mapping(target = "attributeValueCount", expression = "java(attribute.getAttributeValues() != null ? attribute.getAttributeValues().size() : 0)")
    AttributeResponse attributeToAttributeResponse(Attribute attribute);
}
