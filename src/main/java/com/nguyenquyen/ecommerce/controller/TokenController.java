package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.service.ITokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/tokens")
@RequiredArgsConstructor
public class TokenController {

    private final ITokenService tokenService;
}
