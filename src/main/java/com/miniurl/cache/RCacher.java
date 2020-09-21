package com.miniurl.cache;

import org.springframework.stereotype.Component;

@Component
public interface RCacher {

    void put(String key, Object value, long expiryInSec);

    void put(String key, Object value);

    Object get(String key);


}
