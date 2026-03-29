package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.auth.LoginRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import com.nguyenquyen.ecommerce.dto.response.auth.LoginResponse;
import com.nguyenquyen.ecommerce.mapper.UserMapper;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.service.IAuthService;
import com.nguyenquyen.ecommerce.util.JwtUtil;
import com.nguyenquyen.ecommerce.dto.JwtInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.expiration-access-token}")
    private long expirationAccessToken;

    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Get user from authentication
            User user = (User) authentication.getPrincipal();

            // Generate tokens
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            // Convert user to UserResponse
            UserResponse userResponse = userMapper.userToUserResponse(user);

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(expirationAccessToken * 1000)
                    .user(userResponse)
                    .build();

        } catch (Exception e) {
            log.error("Login failed for user: {}", request.getUsername(), e);
            throw new RuntimeException("Email/Phone hoặc mật khẩu không đúng", e);
        }
    }

    @Override
    public void logout(String token) {
        try {
            // Parse token to get JWT info
            JwtInfo jwtInfo = jwtUtil.parseToken(token);

            // Get token expiration time
            long expiresIn = jwtInfo.getExpiresAt().getTime() - System.currentTimeMillis();

            // Add token to Redis blacklist with expiration time
            if (expiresIn > 0) {
                String key = "blacklist:token:" + jwtInfo.getId();
                redisTemplate.opsForValue().set(key, jwtInfo.getSubject(), expiresIn, TimeUnit.MILLISECONDS);
                log.info("Token {} added to blacklist", jwtInfo.getId());
            }

        } catch (Exception e) {
            log.error("Logout failed", e);
            throw new RuntimeException("Logout failed", e);
        }
    }
}





