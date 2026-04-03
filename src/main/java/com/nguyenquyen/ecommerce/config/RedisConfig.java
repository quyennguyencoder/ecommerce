package com.nguyenquyen.ecommerce.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableCaching // Bật tính năng Cache của Spring
public class RedisConfig {

    @Value("${spring.data.redis.host:localhost}")
    private String redisHost;

    @Value("${spring.data.redis.port:6379}")
    private int redisPort;

    /* =======================================================
     * HÀM DÙNG CHUNG: Cấu hình ObjectMapper hỗ trợ Java 8 LocalDateTime
     * ======================================================= */
    private GenericJackson2JsonRedisSerializer customJsonSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    /* =======================================================
     * 1. CẤU HÌNH KẾT NỐI TÁCH BIỆT (DB 0 VÀ DB 1)
     * ======================================================= */
    @Bean
    @Primary
    public RedisConnectionFactory tokenConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(0); // Trỏ vào DB 0 cho Token
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisConnectionFactory cacheConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
        config.setDatabase(1); // Trỏ vào DB 1 cho Spring Cache (@Cacheable)
        return new LettuceConnectionFactory(config);
    }

    /* =======================================================
     * 2. CẤU HÌNH REDIS TEMPLATE CHO TOKEN (Dùng chung customSerializer)
     * ======================================================= */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(tokenConnectionFactory());

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);

        // Sử dụng hàm dùng chung ở đây
        template.setValueSerializer(customJsonSerializer());
        template.setHashValueSerializer(customJsonSerializer());

        template.afterPropertiesSet();
        return template;
    }

    /* =======================================================
     * 3. CẤU HÌNH REDIS CACHE MANAGER CHO @Cacheable (Đã fix lỗi Serializer)
     * ======================================================= */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(2)) // Bạn đang set 2 phút
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // CẬP NHẬT: Sử dụng hàm dùng chung ở đây thay vì serializer mặc định
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(customJsonSerializer()));

        return RedisCacheManager.builder(cacheConnectionFactory())
                .cacheDefaults(cacheConfig)
                .build();
    }
}