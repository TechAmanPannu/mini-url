package com.miniurl.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    public BigInteger getNextCount(){

        final BigInteger initialCount = new BigInteger("1000000");
        String countKey = RedisKeyService.getURLCounterKey();
        ValueOperations ops = redisTemplate.opsForValue();
        if(ops.get(countKey) == null)
            ops.append(RedisKeyService.getURLCounterKey(), String.valueOf(initialCount));
        BigInteger bigInteger = new BigInteger(String.valueOf(ops.get(countKey)));
        BigInteger delta = new BigInteger("1");
        bigInteger.add(delta);
        ops.append(countKey, String.valueOf(bigInteger));
        return bigInteger;

    }

}
