package com.nguyenquyen.ecommerce.service.impl;

import com.nguyenquyen.ecommerce.dto.request.AuthLoginRequest;
import com.nguyenquyen.ecommerce.dto.response.AuthUrlResponse;
import com.nguyenquyen.ecommerce.dto.response.UserResponse;
import com.nguyenquyen.ecommerce.dto.response.LoginResponse;
import com.nguyenquyen.ecommerce.dto.response.RefreshTokenResponse;
import com.nguyenquyen.ecommerce.enums.SocialLoginType;
import com.nguyenquyen.ecommerce.mapper.UserMapper;
import com.nguyenquyen.ecommerce.model.Role;
import com.nguyenquyen.ecommerce.model.SocialAccount;
import com.nguyenquyen.ecommerce.model.User;
import com.nguyenquyen.ecommerce.repository.RoleRepository;
import com.nguyenquyen.ecommerce.repository.SocialAccountRepository;
import com.nguyenquyen.ecommerce.repository.UserRepository;
import com.nguyenquyen.ecommerce.service.IAuthService;
import com.nguyenquyen.ecommerce.util.JwtUtil;
import com.nguyenquyen.ecommerce.dto.JwtInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RestTemplate restTemplate;
    private final RoleRepository roleRepository;
    private final SocialAccountRepository socialAccountRepository;

    @Value("${jwt.expiration-access-token}")
    private long expirationAccessToken;

    @Value("${jwt.expiration-refresh-token}")
    private long expirationRefreshToken;

    // Google OAuth2 Configuration
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;



    // Facebook OAuth2 Configuration
    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    private String facebookClientId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    private String facebookClientSecret;

    @Value("${spring.security.oauth2.client.registration.facebook.redirect-uri}")
    private String facebookRedirectUri;

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

    @Override
    public AuthUrlResponse generateAuthUrl(SocialLoginType socialLoginType) {
        if (socialLoginType == null) {
            log.warn("Social login type is null");
            throw new RuntimeException("Social login type không được để trống");
        }

        String authUrl = switch (socialLoginType) {
            case GOOGLE -> generateGoogleAuthUrl();
            case FACEBOOK -> generateFacebookAuthUrl();
        };

        log.info("Generated auth URL for {}: {}", socialLoginType, authUrl);
        return new AuthUrlResponse(authUrl);
    }

    @Override
    public LoginResponse handleSocialLoginCallback(String code, SocialLoginType socialLoginType) {
        try {
            log.info("Handling {} social login callback", socialLoginType);

            // Step 1: Exchange authorization code for access token
            Map<String, Object> tokenResponse = authenticateAndFetchProfile(code, socialLoginType);


            // Step 2: Get user info from token response
            String email = (String) tokenResponse.get("email");
            String name = (String) tokenResponse.get("name");
            String picture = (String) tokenResponse.get("picture");

            log.info("Fetched user info from {}: email={}", socialLoginType, email);

            // Step 3: Create or update user in database
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> createNewSocialUser(email, name, picture, socialLoginType));

            SocialAccount socialAccount = SocialAccount.builder()
                    .providerId((String) tokenResponse.get("oauth_id"))
                    .providerName(socialLoginType.name())
                    .email((String) tokenResponse.get("email"))
                    .user(user)
                    .build();
            socialAccountRepository.save(socialAccount);


            // Update user picture if needed
            if (picture != null && !picture.isEmpty()) {
                user.setAvatar(picture);
                userRepository.save(user);
            }

            // Step 4: Generate JWT tokens
            String accessToken = jwtUtil.generateAccessToken(user);
            String refreshToken = jwtUtil.generateRefreshToken(user);

            // Step 5: Save refresh token to Redis
            JwtInfo refreshTokenInfo = jwtUtil.parseToken(refreshToken);
            String refreshTokenKey = "refresh_token:" + user.getId();
            long refreshTokenExpiresIn = refreshTokenInfo.getExpiresAt().getTime() - System.currentTimeMillis();
            redisTemplate.opsForValue().set(refreshTokenKey, refreshToken, refreshTokenExpiresIn, TimeUnit.MILLISECONDS);
            log.info("Refresh token saved to Redis for user: {}", user.getId());

            // Convert user to UserResponse
            UserResponse userResponse = userMapper.userToUserResponse(user);

            log.info("Social login successful for user: {}", user.getId());

            return LoginResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(expirationAccessToken * 1000)
                    .user(userResponse)
                    .build();

        } catch (Exception e) {
            log.error("Social login callback failed for type: {}", socialLoginType, e);
            throw new RuntimeException("Social login failed: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> authenticateAndFetchProfile(String code, SocialLoginType socialLoginType) {
        return switch (socialLoginType) {
            case GOOGLE -> authenticateWithGoogle(code);
            case FACEBOOK -> authenticateWithFacebook(code);
        };
    }


    private Map<String, Object> authenticateWithGoogle(String code) {
        try {
            log.info("Exchanging Google authorization code for access token");

            // Step 1: Prepare request to exchange code for token
            String tokenUrl = "https://oauth2.googleapis.com/token";

            // FIX 1: BẮT BUỘC dùng MultiValueMap thay vì HashMap thông thường
            MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
            tokenRequest.add("code", code);
            tokenRequest.add("client_id", googleClientId);
            tokenRequest.add("client_secret", googleClientSecret);
            tokenRequest.add("redirect_uri", googleRedirectUri);
            // Lưu ý quan trọng: Nếu frontend dùng React popup, biến googleRedirectUri này phải là chữ "postmessage"
            tokenRequest.add("grant_type", "authorization_code");

            // FIX 2: Set Content-Type là FORM_URLENCODED
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(tokenRequest, headers);

            // Gửi request lấy token
            Map<String, Object> tokenResponse = restTemplate.postForObject(tokenUrl, entity, Map.class);

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                throw new RuntimeException("Failed to get access token from Google");
            }

            String accessToken = (String) tokenResponse.get("access_token");
            log.info("Successfully obtained Google access token");

            // Step 3: Fetch user info from Google using access token
            String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";

            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);

            HttpEntity<Void> userInfoEntity = new HttpEntity<>(userInfoHeaders);

            // FIX 3: Dùng exchange() thay vì getForObject() để Header được gửi đi
            ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    userInfoEntity,
                    Map.class
            );

            Map<String, Object> userInfo = response.getBody();

            if (userInfo == null) {
                throw new RuntimeException("Failed to fetch user info from Google");
            }

            log.info("Successfully fetched Google user info");

            // Step 4: Extract and normalize user info
            Map<String, Object> result = new HashMap<>();
            result.put("email", userInfo.get("email"));
            result.put("name", userInfo.get("name"));
            result.put("picture", userInfo.get("picture"));
            result.put("provider", "GOOGLE");
            result.put("oauth_id", userInfo.get("sub")); // Google user ID

            return result;

        } catch (Exception e) {
            log.error("Failed to authenticate with Google", e);
            throw new RuntimeException("Google authentication failed: " + e.getMessage(), e);
        }
    }


    private Map<String, Object> authenticateWithFacebook(String code) {
        try {
            log.info("Exchanging Facebook authorization code for access token");

            // Step 1: Exchange authorization code for access token
            String tokenUrl = "https://graph.facebook.com/v18.0/oauth/access_token";

            MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
            tokenRequest.add("client_id", facebookClientId);
            tokenRequest.add("client_secret", facebookClientSecret);
            tokenRequest.add("redirect_uri", facebookRedirectUri);
            tokenRequest.add("code", code);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(tokenRequest, headers);

            // Request access token
            Map<String, Object> tokenResponse = restTemplate.postForObject(tokenUrl, entity, Map.class);

            if (tokenResponse == null || !tokenResponse.containsKey("access_token")) {
                log.error("Failed to get Facebook access token. Response: {}", tokenResponse);
                throw new RuntimeException("Failed to get access token from Facebook");
            }

            String accessToken = (String) tokenResponse.get("access_token");
            log.info("Successfully obtained Facebook access token");

            // Step 2: Fetch user info from Facebook using access token
            String userInfoUrl = "https://graph.facebook.com/v18.0/me?fields=id,email,name,picture&access_token=" + accessToken;

            HttpHeaders userInfoHeaders = new HttpHeaders();
            HttpEntity<Void> userInfoEntity = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<Map> response = restTemplate.exchange(
                    userInfoUrl,
                    HttpMethod.GET,
                    userInfoEntity,
                    Map.class
            );

            Map<String, Object> userInfo = response.getBody();

            if (userInfo == null) {
                log.error("Failed to fetch user info from Facebook");
                throw new RuntimeException("Failed to fetch user info from Facebook");
            }

            log.info("Successfully fetched Facebook user info");

            // Step 3: Extract and normalize user info
            Map<String, Object> result = new HashMap<>();
            result.put("email", userInfo.get("email"));
            result.put("name", userInfo.get("name"));

            // Extract picture URL from nested object
            if (userInfo.containsKey("picture") && userInfo.get("picture") instanceof Map) {
                Map<String, Object> pictureObj = (Map<String, Object>) userInfo.get("picture");
                if (pictureObj.containsKey("data") && pictureObj.get("data") instanceof Map) {
                    Map<String, Object> pictureData = (Map<String, Object>) pictureObj.get("data");
                    result.put("picture", pictureData.get("url"));
                }
            }

            result.put("provider", "FACEBOOK");
            result.put("oauth_id", userInfo.get("id")); // Facebook user ID

            return result;

        } catch (Exception e) {
            log.error("Failed to authenticate with Facebook", e);
            throw new RuntimeException("Facebook authentication failed: " + e.getMessage(), e);
        }
    }


    private User createNewSocialUser(String email, String name, String picture, SocialLoginType socialLoginType) {
        log.info("Creating new user from {} social login: {}", socialLoginType, email);

        User user = User.builder()
                .email(email)
                .name(name)
                .avatar(picture)
                .phone(null) // Phone không có từ OAuth
                .active(true)
                .build();
        Role defaultRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default role not found"));
        user.setRole(defaultRole);
        User savedUser = userRepository.save(user);
        log.info("New user created from {} social login: id={}", socialLoginType, savedUser.getId());

        return savedUser;
    }



    private String generateGoogleAuthUrl() {
        try {
            String scope = URLEncoder.encode("openid email profile", StandardCharsets.UTF_8);
            String redirectUri = URLEncoder.encode(googleRedirectUri, StandardCharsets.UTF_8);

            return String.format(
                    "https://accounts.google.com/o/oauth2/v2/auth?" +
                    "client_id=%s&" +
                    "redirect_uri=%s&" +
                    "response_type=code&" +
                    "scope=%s&" +
                    "access_type=offline&" +
                    "prompt=consent",
                    googleClientId,
                    redirectUri,
                    scope
            );
        } catch (Exception e) {
            log.error("Failed to generate Google auth URL", e);
            throw new RuntimeException("Không thể tạo Google authorization URL", e);
        }
    }

    private String generateFacebookAuthUrl() {
        try {
            String scope = URLEncoder.encode("public_profile,email", StandardCharsets.UTF_8);
            String redirectUri = URLEncoder.encode(facebookRedirectUri, StandardCharsets.UTF_8);

            return String.format(
                    "https://www.facebook.com/v18.0/dialog/oauth?" +
                    "client_id=%s&" +
                    "redirect_uri=%s&" +
                    "scope=%s&" +
                    "response_type=code&" +
                    "auth_type=rerequest",
                    facebookClientId,
                    redirectUri,
                    scope
            );
        } catch (Exception e) {
            log.error("Failed to generate Facebook auth URL", e);
            throw new RuntimeException("Không thể tạo Facebook authorization URL", e);
        }
    }


}





