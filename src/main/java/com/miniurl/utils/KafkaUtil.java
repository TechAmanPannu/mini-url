package com.miniurl.utils;

import org.springframework.kafka.support.KafkaHeaders;

import java.util.HashMap;
import java.util.Map;

public final class KafkaUtil {

    private KafkaUtil(){ }

    public static Map<String, Object> constructHeaders(String topic){
        Map<String, Object> headers = new HashMap<>();
        headers.put(KafkaHeaders.TOPIC, topic);
        return headers;
    }
}
