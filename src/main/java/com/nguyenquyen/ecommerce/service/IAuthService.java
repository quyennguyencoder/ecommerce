package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.auth.LoginRequest;
import com.nguyenquyen.ecommerce.dto.response.auth.LoginResponse;
import com.nguyenquyen.ecommerce.dto.response.auth.RefreshTokenResponse;

public interface IAuthService {
    LoginResponse login(LoginRequest request);

    void logout(String token);

    RefreshTokenResponse refreshToken(String refreshToken);
}
