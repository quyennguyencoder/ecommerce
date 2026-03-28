package com.nguyenquyen.ecommerce.util;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.nguyenquyen.ecommerce.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
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
    private long expirationAccessToken;

    @Value("${jwt.expiration-refresh-token}")
    private long expirationRefreshToken;

    /**
     * Generate JWT access token from User object
     */
    public String generateAccessToken(User user) {
        return generateToken(user, expirationAccessToken);
    }

    /**
     * Generate JWT refresh token from User object
     */
    public String generateRefreshToken(User user) {
        return generateToken(user, expirationRefreshToken);
    }

    /**
     * Generate JWT token from User object
     */
    private String generateToken(User user, long expirationTime) {
        try {
            // Build claims from user
            Map<String, Object> claims = buildClaimsFromUser(user);
            String username = user.getEmail() != null ? user.getEmail() : user.getPhone();

            JWTClaimsSet.Builder claimsSetBuilder = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + expirationTime * 1000));

            // Add custom claims
            if (claims != null) {
                for (Map.Entry<String, Object> entry : claims.entrySet()) {
                    claimsSetBuilder.claim(entry.getKey(), entry.getValue());
                }
            }

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

    /**
     * Build claims from User object
     */
    private Map<String, Object> buildClaimsFromUser(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("phone", user.getPhone());
        claims.put("name", user.getName());
        claims.put("role", user.getRole() != null ? user.getRole().getName() : null);
        return claims;
    }

    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            MACVerifier verifier = new MACVerifier(secretKey.getBytes(StandardCharsets.UTF_8));
            return signedJWT.verify(verifier);
        } catch (ParseException | JOSEException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Extract subject from JWT token
     */
    public String extractSubject(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getSubject();
        } catch (ParseException e) {
            log.warn("Error extracting subject from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extract claim from JWT token
     */
    public Object extractClaim(String token, String claimName) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getClaim(claimName);
        } catch (ParseException e) {
            log.warn("Error extracting claim from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Extract all claims from JWT token
     */
    public Map<String, Object> extractAllClaims(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            return new HashMap<>(claimsSet.getClaims());
        } catch (ParseException e) {
            log.warn("Error extracting claims from token: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Check if token is expired
     */
    public boolean isTokenExpired(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.getJWTClaimsSet().getExpirationTime().before(new Date());
        } catch (ParseException e) {
            log.warn("Error checking token expiration: {}", e.getMessage());
            return true;
        }
    }

}


