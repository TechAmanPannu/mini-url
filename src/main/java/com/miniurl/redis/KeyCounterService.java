package com.miniurl.redis;

import com.miniurl.exception.NotFoundException;
import com.miniurl.utils.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KeyCounterService {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    public BigInteger getNextKeyCount() throws NotFoundException{

        final Long initCount = 10000000000L;
        final int addCountBy = 1;
        BigInteger initialCount = new BigInteger(String.valueOf(initCount));
        String countKey = RedisKeyService.getURLCounterKey();
        final ValueOperations<String, String> ops = redisTemplate.opsForValue();
        if(ops.get(countKey) == null) // todo need to remove this condition once the app is live. counter should be available , if not throw Exception
            ops.set(RedisKeyService.getURLCounterKey(), initialCount.toString());
        String result = String.valueOf(ops.get(countKey));
        if(result == null)
            throw new NotFoundException("counter not found, something is wrong");
        BigInteger bigInteger = new BigInteger(result);
        BigInteger delta = new BigInteger(String.valueOf(addCountBy));
        bigInteger =  bigInteger.add(delta);
        ops.set(countKey, bigInteger.toString());
        return bigInteger;
    }

}
