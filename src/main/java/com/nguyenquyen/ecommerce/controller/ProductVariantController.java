package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.service.IProductVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/product-variants")
@RequiredArgsConstructor
public class ProductVariantController {

    private final IProductVariantService productVariantService;
}
