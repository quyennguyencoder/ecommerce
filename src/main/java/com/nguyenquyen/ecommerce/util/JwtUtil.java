package com.nguyenquyen.ecommerce.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nguyenquyen.ecommerce.dto.JwtInfo;
import com.nguyenquyen.ecommerce.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-access-token}")
    private long expirationAccessToken;

    @Value("${jwt.expiration-refresh-token}")
    private long expirationRefreshToken;

    private final RedisTemplate<String, Object> redisTemplate;


    public String generateAccessToken(User user) {
        return generateToken(user, expirationAccessToken);
    }

    public String generateRefreshToken(User user) {
        return generateToken(user, expirationRefreshToken);
    }

    private String generateToken(User user, long expirationTime) {
        try {
            String username = user.getEmail() != null ? user.getEmail() : user.getPhone();

            JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                    .subject(username)
                    .jwtID(UUID.randomUUID().toString())
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expirationTime * 1000))
                    .claim("roles", user.getRole() != null ? user.getRole().getName() : null);

            JWTClaimsSet claimsSet = claimsSetBuilder.build();

            // Create JWT header
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            // Create signed JWT
            SignedJWT signedJWT = new SignedJWT(header, claimsSet);

            // Sign the JWT
            MACSigner signer = new MACSigner(secretKey.getBytes(StandardCharsets.UTF_8));
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error generating JWT token", e);
            throw new RuntimeException("Error generating JWT token", e);
        }
    }



    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
//            MACVerifier verifier = new MACVerifier(secretKey.getBytes(StandardCharsets.UTF_8));
//            // Verify signature
//            if (!signedJWT.verify(verifier)) {
//                return false;
//            }

            // Check if token is in blacklist
            String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
            String blacklistKey = "blacklist:token:" + jwtId;
            if (Boolean.TRUE.equals(redisTemplate.hasKey(blacklistKey))) {
                log.warn("Token with ID {} is in blacklist", jwtId);
                return false;
            }

            return true;
        } catch (ParseException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    public JwtInfo parseToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            return JwtInfo.builder()
                    .id(claimsSet.getJWTID())
                    .subject(claimsSet.getSubject())
                    .roles((String) claimsSet.getClaim("roles"))
                    .issuedAt(claimsSet.getIssueTime())
                    .expiresAt(claimsSet.getExpirationTime())
                    .build();
        } catch (ParseException e) {
            log.error("Error parsing JWT token", e);
            throw new RuntimeException("Error parsing JWT token", e);
        }
    }
}





