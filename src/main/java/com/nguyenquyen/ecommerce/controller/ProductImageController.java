package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.service.IProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/product-images")
@RequiredArgsConstructor
public class ProductImageController {

    private final IProductImageService productImageService;
}
