package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.auth.LoginRequest;
import com.nguyenquyen.ecommerce.dto.response.LoginResponse;
import com.nguyenquyen.ecommerce.dto.response.RefreshTokenResponse;

public interface IAuthService {
    LoginResponse login(LoginRequest request);

    void logout(String token);

    RefreshTokenResponse refreshToken(String refreshToken);
}
