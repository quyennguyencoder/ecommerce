package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.service.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/cart-items")
@RequiredArgsConstructor
public class CartItemController {

    private final ICartItemService cartItemService;
}
