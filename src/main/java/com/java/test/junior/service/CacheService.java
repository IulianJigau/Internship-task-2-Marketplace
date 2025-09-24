package com.java.test.junior.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CacheService {
    private static final String PAGE_TAIL = ":page";
    private static final Duration CACHE_TTL = Duration.ofMinutes(15);

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public <T> T popItem(String key, Class<T> type) {
        String itemString = redisTemplate.opsForList().leftPop(key);
        if (itemString == null) {
            return null;
        }

        redisTemplate.expire(key, CACHE_TTL);
        redisTemplate.expire(key + PAGE_TAIL, CACHE_TTL);

        try {
            return objectMapper.readValue(itemString, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize item from Redis", e);
        }
    }

    public void refreshCache(String key) {
        redisTemplate.delete(key);
        redisTemplate.delete(key + PAGE_TAIL);
    }

    public int getPageCount(String key) {
        String count = redisTemplate.opsForValue().get(key + PAGE_TAIL);
        return count != null ? Integer.parseInt(count) : 0;
    }

    public <T> void pushItems(String key, List<T> items) {
        if (items.isEmpty()) return;

        redisTemplate.opsForValue().increment(key + PAGE_TAIL);
        redisTemplate.expire(key + PAGE_TAIL, CACHE_TTL);

        List<String> jsonItems = items.stream().map(item -> {
            try {
                return objectMapper.writeValueAsString(item);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Failed to serialize item to string", e);
            }
        }).collect(Collectors.toList());

        redisTemplate.opsForList().rightPushAll(key, jsonItems);
        redisTemplate.expire(key, CACHE_TTL);
    }
}

