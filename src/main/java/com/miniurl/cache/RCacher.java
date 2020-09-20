package com.miniurl.cache;

import org.springframework.stereotype.Component;

@Component
public interface RCacher {

    void put(String key, Object value, long expiryInSec);

    Object get(String key);

    void remove(String key);


}
