package com.miniurl.redis;

public final class RedisKeyService {

    private final static String BASE_KEY = "miniurl";

    public static String getURLCounterKey(){
        return BASE_KEY + ":" + "urlcounter";
    }

}
