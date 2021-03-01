package com.office2easy.common.utils;

import com.office2easy.common.entity.SystemConst;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class ShiroUtils {
    private final RedisTemplate redisTemplate;

    @Autowired
    public ShiroUtils(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void removePermission() {
        redisTemplate.delete(SystemConst.AUTHORIZATION_CACHE_NAME);
    }
}
