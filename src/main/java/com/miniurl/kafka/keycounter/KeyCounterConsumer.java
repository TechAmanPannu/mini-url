package com.miniurl.kafka.keycounter;

import com.miniurl.zookeeper.keycounter.KeyCounter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KeyCounterConsumer {

    @Autowired
    private KeyCounter keyCounter;

    @KafkaListener(topics = {KeyCounterTopic.COUNTER_ADD_RANGES}, groupId = "${kafka.consumer.group.id}")
    public void processAddingAllRanges(String payload) {

        log.info("payload : "+payload);

        keyCounter.addAllRanges();

        log.info("Ranges created Successully, please verify connecting to zookeeper client using ... \n" +
                "(1. kubectl exec -it mini-url-zookeeper-0 -- zkCli.sh) \n" +
                "2. ls /mini_url/key_ranges : To get all ranges \n" +
                "3. get /mini_url/key_ranges/{range-name}: To get sub ranges in it, for instance get /mini_url/key_ranges/range0000000070");

    }
}
