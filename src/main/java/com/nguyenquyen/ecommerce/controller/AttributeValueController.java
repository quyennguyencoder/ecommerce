package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.service.IAttributeValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/attribute-values")
@RequiredArgsConstructor
public class AttributeValueController {

    private final IAttributeValueService attributeValueService;
}
