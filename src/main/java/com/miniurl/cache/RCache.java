package com.miniurl.cache;

import com.miniurl.utils.ObjUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

public class RCache implements RCacher{

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void put(String key, Object value, long expiryInSec) {

        if(ObjUtil.isBlank(key) || value == null)
            return;

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, value, expiryInSec, TimeUnit.SECONDS);
    }

    @Override
    public Object get(String key) {

        if(ObjUtil.isBlank(key) )
            return null;

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    @Override
    public void remove(String key) {

        if(ObjUtil.isBlank(key) )
            return;

        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
        ops.set(key, "DEFAULT", 1, TimeUnit.NANOSECONDS);
    }
}
