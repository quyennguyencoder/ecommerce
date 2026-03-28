package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.auth.LoginRequest;
import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByEmailRequest;
import com.nguyenquyen.ecommerce.dto.request.auth.RegisterByPhoneRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import com.nguyenquyen.ecommerce.dto.response.auth.LoginResponse;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
}
