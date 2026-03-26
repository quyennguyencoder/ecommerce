package com.nguyenquyen.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // Khởi tạo một RedisTemplate với Key là String, Value là Object
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Gắn kết nối (Spring Boot sẽ tự động tiêm connectionFactory từ application.yml vào đây)
        template.setConnectionFactory(connectionFactory);

        // 1. Cấu hình Serializer cho Key: Chuyển Key thành String bình thường
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer); // Dùng cho cấu trúc dữ liệu Hash

        // 2. Cấu hình Serializer cho Value: Chuyển Object Java thành JSON
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer(jsonSerializer);
        template.setHashValueSerializer(jsonSerializer); // Dùng cho cấu trúc dữ liệu Hash

        // Cập nhật các thuộc tính vừa cấu hình
        template.afterPropertiesSet();

        return template;
    }
}