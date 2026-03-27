package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.attributeValue.CreateAttributeValueRequest;
import com.nguyenquyen.ecommerce.dto.request.attributeValue.UpdateAttributeValueRequest;
import com.nguyenquyen.ecommerce.dto.response.AttributeValueResponse;
import com.nguyenquyen.ecommerce.mapper.AttributeValueMapper;
import com.nguyenquyen.ecommerce.model.Attribute;
import com.nguyenquyen.ecommerce.model.AttributeValue;
import com.nguyenquyen.ecommerce.repository.AttributeRepository;
import com.nguyenquyen.ecommerce.repository.AttributeValueRepository;
import com.nguyenquyen.ecommerce.service.IAttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeValueService implements IAttributeValueService {

    private final AttributeValueRepository attributeValueRepository;
    private final AttributeRepository attributeRepository;
    private final AttributeValueMapper attributeValueMapper;

    @Override
    public List<AttributeValueResponse> getAllAttributeValues() {
        List<AttributeValue> attributeValues = attributeValueRepository.findAll();
        return attributeValues.stream()
                .map(attributeValueMapper::attributeValueToAttributeValueResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeValueResponse getAttributeValueById(Long id) {
        AttributeValue attributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Giá trị thuộc tính không tồn tại với id: " + id));
        return attributeValueMapper.attributeValueToAttributeValueResponse(attributeValue);
    }

    @Override
    public List<AttributeValueResponse> getAttributeValuesByAttributeId(Long attributeId) {
        if (!attributeRepository.existsById(attributeId)) {
            throw new RuntimeException("Thuộc tính không tồn tại với id: " + attributeId);
        }
        List<AttributeValue> attributeValues = attributeValueRepository.findByAttributeId(attributeId);
        return attributeValues.stream()
                .map(attributeValueMapper::attributeValueToAttributeValueResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeValueResponse createAttributeValue(CreateAttributeValueRequest request) {
        Attribute attribute = attributeRepository.findById(request.getAttributeId())
                .orElseThrow(() -> new RuntimeException("Thuộc tính không tồn tại với id: " + request.getAttributeId()));

        AttributeValue attributeValue = AttributeValue.builder()
                .value(request.getValue())
                .attribute(attribute)
                .build();

        AttributeValue savedAttributeValue = attributeValueRepository.save(attributeValue);
        return attributeValueMapper.attributeValueToAttributeValueResponse(savedAttributeValue);
    }

    @Override
    public AttributeValueResponse updateAttributeValue(Long id, UpdateAttributeValueRequest request) {
        AttributeValue existingAttributeValue = attributeValueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Giá trị thuộc tính không tồn tại với id: " + id));

        if (request.getValue() != null && !request.getValue().isEmpty()) {
            existingAttributeValue.setValue(request.getValue());
        }

        if (request.getAttributeId() != null) {
            Attribute attribute = attributeRepository.findById(request.getAttributeId())
                    .orElseThrow(() -> new RuntimeException("Thuộc tính không tồn tại với id: " + request.getAttributeId()));
            existingAttributeValue.setAttribute(attribute);
        }

        AttributeValue updatedAttributeValue = attributeValueRepository.save(existingAttributeValue);
        return attributeValueMapper.attributeValueToAttributeValueResponse(updatedAttributeValue);
    }

    @Override
    public void deleteAttributeValue(Long id) {
        if (!attributeValueRepository.existsById(id)) {
            throw new RuntimeException("Giá trị thuộc tính không tồn tại với id: " + id);
        }
        attributeValueRepository.deleteById(id);
    }
}
