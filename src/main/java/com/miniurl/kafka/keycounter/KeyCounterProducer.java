package com.miniurl.kafka.keycounter;

import com.miniurl.zookeeper.leaderselector.LeaderSelector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class KeyCounterProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    static final String ADD_COUNTER_RANGES = "add_counter_ranges";

    public void addCounterRanges() {

        kafkaTemplate.send(new GenericMessage("Please start adding counter msgs ", constructHeaders(ADD_COUNTER_RANGES)));
        log.info("sent adding ranges to Kafka Topic - " + ADD_COUNTER_RANGES);
    }

    public static  Map<String, Object> constructHeaders(String topic){
        Map<String, Object> headers = new HashMap<>();
        headers.put(KafkaHeaders.TOPIC, topic);
        return headers;
    }


}
