package com.miniurl.kafka.keycounter;

import com.miniurl.zookeeper.keycounter.KeyCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeyCounterConsumer {

    @KafkaListener(topics = {KeyCounterProducer.ADD_COUNTER_RANGES}, groupId = "${kafka.consumer.group.id}")
    public void processAddingAllRanges(String payload) {
        log.info("received adding all ranges ");
        log.info("start processing");
        log.info("payload : "+payload);


    }
}
