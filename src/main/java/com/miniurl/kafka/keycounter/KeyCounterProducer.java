package com.miniurl.kafka.keycounter;

import com.miniurl.utils.KafkaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import static com.miniurl.kafka.keycounter.KeyCounterTopic.COUNTER_ADD_RANGES;

@Slf4j
@Service
public class KeyCounterProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void addCounterRanges() {

        kafkaTemplate.send(new GenericMessage("Please start adding counter msgs ", KafkaUtil.constructHeaders(COUNTER_ADD_RANGES)));
        log.info("sent adding ranges to kafka topic - " + COUNTER_ADD_RANGES);
    }




}
