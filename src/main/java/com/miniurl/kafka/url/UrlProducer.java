package com.miniurl.kafka.url;

import com.miniurl.entity.Url;
import com.miniurl.utils.KafkaUtil;
import com.miniurl.utils.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void createUrl(Url url) {

        kafkaTemplate.send(new GenericMessage(ObjUtil.getJson(url), KafkaUtil.constructHeaders(UrlTopic.URL_CREATE)));
        log.info("msg sent to kafka topic - " + UrlTopic.URL_CREATE);
    }
}
