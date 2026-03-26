package com.nguyenquyen.ecommerce.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash("blacklist_token") // BẮT BUỘC: Xác định prefix cho Key trong Redis
public class Token {
    @Id
    private String jwtId;
    @TimeToLive(unit = TimeUnit.SECONDS) // Nên để SECONDS (giây) hoặc MILLISECONDS cho JWT
    private Long expirationTime;
}