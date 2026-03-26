package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.service.IShippingDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/shipping-details")
@RequiredArgsConstructor
public class ShippingDetailController {

    private final IShippingDetailService shippingDetailService;
}
