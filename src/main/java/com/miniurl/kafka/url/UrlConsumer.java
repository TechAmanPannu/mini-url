package com.miniurl.kafka.url;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.Url;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UrlConsumer {

    @Autowired
    private UrlDao urlDao;

    @KafkaListener(topics = {UrlTopic.CREATE_URL_INDEXES}, groupId = "${kafka.consumer.group.id}")
    public void createUrlIndexes(String payload) {

        Url url = ObjUtil.safeConvertJson(payload, Url.class);

        log.info("creating indexes for url");
        log.info("url Id : "+url.getId());


    }

    @KafkaListener(topics = {UrlTopic.UPDATE_URL_INDEXES}, groupId = "${kafka.consumer.group.id}")
    public void updateUrlIndexes(String payload) {

        List<Url> urls = Url.asList(payload);

        Preconditions.checkArgument(ObjUtil.isNullOrEmpty(urls), "Invalid urls to update");
        Preconditions.checkArgument(urls.size() < 2, "old and new urls are not valid to update indexes");

        log.info("updating indexes for url");
        log.info("urls :"+ObjUtil.getJson(urls));

        Url oldUrl = urls.get(0);
        Url newUrl = urls.get(1);


    }

}

