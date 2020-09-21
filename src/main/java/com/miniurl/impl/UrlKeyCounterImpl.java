package com.miniurl.impl;

import com.miniurl.dao.UrlKeyCounterDao;
import com.miniurl.entity.UrlKeyCounter;
import com.miniurl.repositories.UrlKeyCounter.UrlKeyCounterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UrlKeyCounterImpl implements UrlKeyCounterDao {

    @Autowired
    private UrlKeyCounterRepository urlKeyCounterRepository;

    private final static String urlKeyCounterPrimaryKey = "mini_url_key_Counter";

    @Override
    public UrlKeyCounter incrementByOne() {
        return incrementByOne(urlKeyCounterPrimaryKey);
    }


    @CachePut(value = "url_key_counter", key = "$id")
    private UrlKeyCounter incrementByOne(String id){

        try {
           return urlKeyCounterRepository.updateCounterValue(id);
        }catch (Exception e){
            log.error("Exception while incrementing counter ", e.getMessage(), e);
            return null;
        }
    }
}
