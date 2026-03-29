package com.nguyenquyen.ecommerce.config;

import com.nguyenquyen.ecommerce.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.secret-key}")
    private String secretKey;

    private final JwtUtil jwtUtil;
    private NimbusJwtDecoder nimbusJwtDecoder;

    private void initializeJwtDecoder() {
        if (nimbusJwtDecoder == null) {
            SecretKey secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HS256");
            this.nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec).build();
        }
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            initializeJwtDecoder();

            // Validate token (signature + blacklist check)
            if (!jwtUtil.validateToken(token)) {
                throw new JwtException("Token is invalid or has been revoked");
            }

            // Decode token using NimbusJwtDecoder
            return nimbusJwtDecoder.decode(token);
        } catch (JwtException e) {
            throw e; // Re-throw JwtException
        } catch (Exception e) {
            log.error("Error decoding JWT token", e);
            throw new JwtException("Invalid JWT token", e);
        }
    }
}
