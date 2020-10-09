package com.miniurl.kafka.url;

import com.miniurl.entity.Url;
import com.miniurl.utils.KafkaUtil;
import com.miniurl.utils.ObjUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UrlProducer {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void createUrlIndexes(Url url) {

        kafkaTemplate.send(new GenericMessage(ObjUtil.getJson(url), KafkaUtil.constructHeaders(UrlTopic.CREATE_URL_INDEXES)));
        log.info("msg sent to kafka topic - " + UrlTopic.CREATE_URL_INDEXES);
    }

    public void updateUrlIndexes(Url oldUrl, Url newUrl){

        List<Url> urls = new ArrayList<>();
        urls.add(oldUrl);
        urls.add(newUrl);

        kafkaTemplate.send(new GenericMessage(ObjUtil.getJson(urls), KafkaUtil.constructHeaders(UrlTopic.UPDATE_URL_INDEXES)));
        log.info("msg sent to kafka topic - " + UrlTopic.UPDATE_URL_INDEXES);
    }
}

