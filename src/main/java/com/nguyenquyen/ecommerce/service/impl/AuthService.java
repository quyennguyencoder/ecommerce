package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.AuthLoginRequest;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import com.nguyenquyen.ecommerce.dto.response.LoginResponse;
import com.nguyenquyen.ecommerce.dto.response.RefreshTokenResponse;
import com.nguyenquyen.ecommerce.mapper.UserMapper;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.expiration-access-token}")
    private long expirationAccessToken;

    @Value("${jwt.expiration-refresh-token}")
    private long expirationRefreshToken;

    @Override
    public LoginResponse login(AuthLoginRequest request) {
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmailOrPhone(),
                            request.getPassword()
                    )
            );

            // Get user from authentication
            User user = (User) authentication.getPrincipal();

            // Generate tokens
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            // Save refresh token to Redis
            JwtInfo refreshTokenInfo = jwtUtil.parseToken(refreshToken);
            String refreshTokenKey = "refresh_token:" + user.getId();
            long refreshTokenExpiresIn = refreshTokenInfo.getExpiresAt().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, refreshTokenExpiresIn, TimeUnit.MILLISECONDS);
            log.info("Refresh token saved to Redis for user: {}", user.getId());

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
            log.error("Login failed for user: {}", request.getEmailOrPhone(), e);
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

    @Override
    public RefreshTokenResponse refreshToken(String refreshToken) {
        try {
            // Validate refresh token (check signature and blacklist)
            if (!jwtUtil.validateToken(refreshToken)) {
                log.warn("Invalid or blacklisted refresh token");
                throw new RuntimeException("Refresh token is invalid or has been revoked");
            }

            // Parse refresh token to get user info
            JwtInfo jwtInfo = jwtUtil.parseToken(refreshToken);
            Long userId = jwtInfo.getUserId();

            log.info("Refreshing token for user ID: {}", userId);

            // Verify refresh token exists in Redis
            String refreshTokenKey = "refresh_token:" + userId;
            Object storedRefreshToken = redisTemplate.opsForValue().get(refreshTokenKey);

            if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
                log.warn("Refresh token not found or mismatch in Redis for user: {}", userId);
                throw new RuntimeException("Refresh token is invalid");
            }

            // Get user from database
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Generate new tokens
            String newAccessToken = jwtUtil.generateAccessToken(user);
            String newRefreshToken = jwtUtil.generateRefreshToken(user);

            // Save new refresh token to Redis
            JwtInfo newRefreshTokenInfo = jwtUtil.parseToken(newRefreshToken);
            long newRefreshTokenExpiresIn = newRefreshTokenInfo.getExpiresAt().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(refreshTokenKey, newRefreshToken, newRefreshTokenExpiresIn, TimeUnit.MILLISECONDS);
            log.info("New refresh token saved to Redis for user: {}", userId);

            return RefreshTokenResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(expirationAccessToken * 1000)
                    .build();

        } catch (Exception e) {
            log.error("Token refresh failed", e);
            throw new RuntimeException("Token refresh failed: " + e.getMessage(), e);
        }
    }
}





