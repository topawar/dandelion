package com.topawar.web.manager;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理器
 *
 * @author topawar
 */
@Component
public class CacheManager {
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    Cache<String, Object> localCache = Caffeine.newBuilder()
            .expireAfterWrite(30,TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();

    /**
     * 放置缓存
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        localCache.put(key, value);
        redisTemplate.opsForValue().set(key, value, 30L, TimeUnit.MINUTES);
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public Object get(String key) {
        Object value = localCache.getIfPresent(key);
        if (null == value) {
            value = redisTemplate.opsForValue().get(key);
        }
        return value;
    }

    /**
     * 删除缓存
     * @param key
     */
    public void remove(String key) {
        localCache.invalidate(key);
        redisTemplate.delete(key);
    }
}
