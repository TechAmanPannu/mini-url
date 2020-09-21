package com.miniurl.cache;

import com.miniurl.utils.ObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RCache implements RCacher{

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    public void put(String key, Object value, long expiryInSec) {

        if(ObjUtil.isBlank(key) || value == null)
            return;

        key = getKeyWithRCacheNamespace(key);
        ValueOperations<Object, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, expiryInSec, TimeUnit.SECONDS);
    }

    private String getKeyWithRCacheNamespace(String key) {
        String namespace = "RCache::";
        key = namespace + key;
        return key;
    }

    @Override
    public void put(String key, Object value) {

        if(ObjUtil.isBlank(key) || value == null)
            return;

        key = getKeyWithRCacheNamespace(key);
        ValueOperations<Object, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value);
    }

    @Override
    public Object get(String key) {

        if(ObjUtil.isBlank(key) )
            return null;

        key = getKeyWithRCacheNamespace(key);
        ValueOperations<Object, Object> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

}
