package com.nguyenquyen.ecommerce.service;

import com.nguyenquyen.ecommerce.dto.request.AuthLoginRequest;
import com.nguyenquyen.ecommerce.dto.response.LoginResponse;
import com.nguyenquyen.ecommerce.dto.response.RefreshTokenResponse;

public interface IAuthService {
    LoginResponse login(AuthLoginRequest request);

    void logout(String token);

    RefreshTokenResponse refreshToken(String refreshToken);
}
