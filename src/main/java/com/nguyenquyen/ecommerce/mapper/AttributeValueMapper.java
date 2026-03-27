package com.nguyenquyen.ecommerce.mapper;

import com.nguyenquyen.ecommerce.dto.response.AttributeValueResponse;
import com.nguyenquyen.ecommerce.model.AttributeValue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttributeValueMapper {

    @Mapping(target = "attributeId", source = "attribute.id")
    @Mapping(target = "attributeName", source = "attribute.name")
    @Mapping(target = "variantCount", expression = "java(attributeValue.getVariants() != null ? attributeValue.getVariants().size() : 0)")
    AttributeValueResponse attributeValueToAttributeValueResponse(AttributeValue attributeValue);
}
