package com.nguyenquyen.ecommerce.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration-access-token}")
    private long accessTokenExpiration;

    @Value("${jwt.expiration-refresh-token}")
    private long refreshTokenExpiration;

    /**
     * Tạo Access Token
     */
    public String generateAccessToken(String subject) {
        return generateAccessToken(subject, new HashMap<>());
    }

    /**
     * Tạo Access Token với claims tùy chỉnh
     */
    public String generateAccessToken(String subject, Map<String, Object> claims) {
        try {
            Instant now = Instant.now();
            Instant expirationTime = now.plusSeconds(accessTokenExpiration);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expirationTime))
                    .build();

            // Thêm custom claims
            if (!claims.isEmpty()) {
                JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder(claimsSet);
                claims.forEach(builder::claim);
                claimsSet = builder.build();
            }

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            MACSigner signer = new MACSigner(secretKey.getBytes());
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error generating access token: {}", e.getMessage());
            throw new RuntimeException("Failed to generate access token", e);
        }
    }

    /**
     * Tạo Refresh Token
     */
    public String generateRefreshToken(String subject) {
        try {
            Instant now = Instant.now();
            Instant expirationTime = now.plusSeconds(refreshTokenExpiration);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(subject)
                    .issueTime(Date.from(now))
                    .expirationTime(Date.from(expirationTime))
                    .claim("type", "refresh")
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            MACSigner signer = new MACSigner(secretKey.getBytes());
            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (JOSEException e) {
            log.error("Error generating refresh token: {}", e.getMessage());
            throw new RuntimeException("Failed to generate refresh token", e);
        }
    }

    /**
     * Xác thực và lấy subject từ token
     */
    public String getSubjectFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (validateToken(token)) {
                return signedJWT.getJWTClaimsSet().getSubject();
            }
        } catch (ParseException e) {
            log.error("Error parsing token: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Lấy claim từ token
     */
    public Object getClaimFromToken(String token, String claimName) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            if (validateToken(token)) {
                return signedJWT.getJWTClaimsSet().getClaim(claimName);
            }
        } catch (ParseException e) {
            log.error("Error parsing token: {}", e.getMessage());
        }
        return null;
    }

    /**
     * Xác thực token
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);

            // Kiểm tra signature
            MACVerifier verifier = new MACVerifier(secretKey.getBytes());
            if (!signedJWT.verify(verifier)) {
                log.warn("Token signature verification failed");
                return false;
            }

            // Kiểm tra expiration
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            if (claimsSet.getExpirationTime().before(new Date())) {
                log.warn("Token has expired");
                return false;
            }

            return true;
        } catch (ParseException | JOSEException e) {
            log.error("Error validating token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Lấy expiration time từ token
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getExpirationTime();
        } catch (ParseException e) {
            log.error("Error parsing token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Kiểm tra token có hết hạn không
     */
    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate != null && expirationDate.before(new Date());
    }
}
