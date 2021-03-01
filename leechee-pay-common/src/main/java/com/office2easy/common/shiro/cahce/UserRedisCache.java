package com.office2easy.common.shiro.cahce;


import com.office2easy.common.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Set;

@Slf4j
@Component
public class UserRedisCache<k, v> implements Cache<k, v> {


    private String cacheName;



    public UserRedisCache(String cacheName) {
        this.cacheName = cacheName;
    }

    public UserRedisCache() {
    }

    @Override
    public v get(k k) throws CacheException {
        return (v) getRedisTemplate().opsForHash().get(cacheName, k.toString());
    }

    @Override
    public v put(k k, v v) throws CacheException {
        getRedisTemplate().opsForHash().put(cacheName, k.toString(), v);
        return null;
    }

    @Override
    public v remove(k k) throws CacheException {
        getRedisTemplate().opsForHash().delete(cacheName, k.toString());
        return null;
    }

    @Override
    public void clear() throws CacheException {
        getRedisTemplate().opsForHash().delete(cacheName);
    }

    @Override
    public int size() {
        return getRedisTemplate().opsForHash().size(cacheName).intValue();
    }

    @Override
    public Set<k> keys() {
        return (Set<k>) getRedisTemplate().opsForHash().keys(cacheName);
    }

    @Override
    public Collection<v> values() {
        return (Collection<v>) getRedisTemplate().opsForHash().values(cacheName);
    }

    private RedisTemplate<String,Object> getRedisTemplate(){
        return (RedisTemplate<String, Object>) ApplicationContextUtils.getBean("redisTemplate");
    }

}
