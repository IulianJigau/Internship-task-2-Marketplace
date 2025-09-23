package com.java.test.junior.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CacheService {
    private final StringRedisTemplate redisTemplate;

    public Long nextId(String key, Boolean refresh) {
        if (refresh) {
            redisTemplate.delete(key);
        }
        Long newValue = redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, Duration.ofMinutes(15));
        return newValue;
    }
}
