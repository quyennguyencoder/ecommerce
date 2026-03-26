package com.nguyenquyen.ecommerce.controller;

import com.nguyenquyen.ecommerce.service.ISocialAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.prefix}/social-accounts")
@RequiredArgsConstructor
public class SocialAccountController {

    private final ISocialAccountService socialAccountService;
}
