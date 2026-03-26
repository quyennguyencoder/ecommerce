package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.service.IAttributeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/attributes")
@RequiredArgsConstructor
public class AttributeController {

    private final IAttributeService attributeService;
}
