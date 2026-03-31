package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.AttributeCreateRequest;
import com.nguyenquyen.ecommerce.dto.request.AttributeUpdateRequest;
import com.nguyenquyen.ecommerce.dto.response.AttributeResponse;
import com.nguyenquyen.ecommerce.mapper.AttributeMapper;
import com.nguyenquyen.ecommerce.model.Attribute;
import com.nguyenquyen.ecommerce.repository.AttributeRepository;
import com.nguyenquyen.ecommerce.service.IAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttributeService implements IAttributeService {

    private final AttributeRepository attributeRepository;
    private final AttributeMapper attributeMapper;

    @Override
    public List<AttributeResponse> getAllAttributes() {
        List<Attribute> attributes = attributeRepository.findAll();
        return attributes.stream()
                .map(attributeMapper::attributeToAttributeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AttributeResponse getAttributeById(Long id) {
        Attribute attribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thuộc tính không tồn tại với id: " + id));
        return attributeMapper.attributeToAttributeResponse(attribute);
    }

    @Override
    public AttributeResponse createAttribute(AttributeCreateRequest request) {
        Attribute attribute = Attribute.builder()
                .name(request.getName())
                .build();

        Attribute savedAttribute = attributeRepository.save(attribute);
        return attributeMapper.attributeToAttributeResponse(savedAttribute);
    }

    @Override
    public AttributeResponse updateAttribute(Long id, AttributeUpdateRequest request) {
        Attribute existingAttribute = attributeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Thuộc tính không tồn tại với id: " + id));

        if (request.getName() != null && !request.getName().isEmpty()) {
            existingAttribute.setName(request.getName());
        }

        Attribute updatedAttribute = attributeRepository.save(existingAttribute);
        return attributeMapper.attributeToAttributeResponse(updatedAttribute);
    }

    @Override
    public void deleteAttribute(Long id) {
        if (!attributeRepository.existsById(id)) {
            throw new RuntimeException("Thuộc tính không tồn tại với id: " + id);
        }
        attributeRepository.deleteById(id);
    }
}
