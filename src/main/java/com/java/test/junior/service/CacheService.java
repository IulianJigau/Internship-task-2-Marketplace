package com.java.test.junior.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CacheService {
    private static final Duration CACHE_TTL = Duration.ofMinutes(15);

    private final StringRedisTemplate redisTemplate;

    public Long getOffset(String key) {
        String offset = redisTemplate.opsForValue().get(key);
        return offset != null ? Long.parseLong(offset) : 0;
    }

    public void setOffset(String key, Integer delta) {
        redisTemplate.opsForValue().increment(key, delta);
        redisTemplate.expire(key, CACHE_TTL);
    }

    public void refreshCache(String key) {
        redisTemplate.delete(key);
    }

}

