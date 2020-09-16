package com.miniurl.redis;

import com.miniurl.model.Text;
import com.miniurl.utils.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Slf4j
@Service
public class RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    public BigInteger getNextCount(){

        Text initialCount = new Text("1000000");
        final String countKey = RedisKeyService.getURLCounterKey();
        final ValueOperations ops = redisTemplate.opsForValue();

        log.info("Msg : "+ObjUtil.getJson(initialCount));

        if(ops.get(countKey) == null) // todo need to remove this condition once the app is live. counter should be available , if not throw Exception
            ops.append(RedisKeyService.getURLCounterKey(), ObjUtil.getJson(initialCount));

        log.info("coming here ; ");
        Text retrivedCount = ObjUtil.safeConvertJson(ObjUtil.getJson(ops.get(countKey)), Text.class);
        BigInteger bigInteger = new BigInteger(retrivedCount.getValue());
        BigInteger delta = new BigInteger("1");
        bigInteger.add(delta);
        retrivedCount.setValue(bigInteger.toString());
        ops.append(countKey, ObjUtil.getJson(retrivedCount));
        return bigInteger;
    }

}
