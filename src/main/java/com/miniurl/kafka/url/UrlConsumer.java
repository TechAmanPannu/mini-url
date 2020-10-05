package com.miniurl.kafka.url;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.Url;
import com.miniurl.entity.indexes.url.UrlCreatedInDescByUser;
import com.miniurl.entity.indexes.url.UrlExpiresAtWithCreatedUser;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlConsumer {

    @Autowired
    private UrlDao urlDao;

    @KafkaListener(topics = {UrlTopic.UPDATE_URL_INDEXES}, groupId = "${kafka.consumer.group.id}")
    public void updateUrlIndexes(String payload) {

        log.info("url consumer started updating all url indexes");

        Url url = ObjUtil.safeConvertJson(payload, Url.class);

        Preconditions.checkArgument(url == null, "Invalid url to create");

        urlDao.save(new UrlCreatedInDescByUser(url.getCreatedBy(), url.getCreatedAt(), url.getId()));
        urlDao.save(new UrlExpiresAtWithCreatedUser(url.getCreatedBy(), url.getExpiresAt(), url.getId()));

    }
}

