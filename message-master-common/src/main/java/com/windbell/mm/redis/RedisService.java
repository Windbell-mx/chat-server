package com.windbell.mm.redis;

import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    // 设置缓存
    public void set(String key, Object value, long time) {
        redisTemplate.opsForValue().set(key, JSON.toJSONString(value), time, TimeUnit.SECONDS);
    }

    // 获取缓存
    public <T> T get(String key, Class<T> clazz) {
        String value = redisTemplate.opsForValue().get(key);
        return value != null ? JSON.parseObject(value, clazz) : null;
    }

    // 删除缓存
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}

