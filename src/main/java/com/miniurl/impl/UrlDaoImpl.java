package com.miniurl.impl;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.url.Url;
import com.miniurl.entity.url.UrlCreatedInDescByUser;
import com.miniurl.exception.EntityException;
import com.miniurl.exception.enums.EntityErrorCode;
import com.miniurl.model.request.UrlRequest;
import com.miniurl.redis.KeyCounterService;
import com.miniurl.repositories.url.UrlCreatedInDescByUserRepository;
import com.miniurl.repositories.url.UrlRepository;
import com.miniurl.utils.EncodeUtil;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UrlDaoImpl implements UrlDao {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlCreatedInDescByUserRepository urlCreatedInDescByUserRepository;

    @Autowired
    private KeyCounterService keyCounterService;

    @Override
    public String create(UrlRequest urlRequest) throws EntityException {

        Preconditions.checkArgument(urlRequest == null, "Invalid url to save");
        Preconditions.checkArgument(ObjUtil.isBlank(urlRequest.getUrl()), "Invalid url string to create url");

        Url url = new Url(urlRequest.getUrl());
        url.setAccessType("PUBLIC");
        url.setCreatedBy(urlRequest.getUserId());
        url.setExpiresAt((20 * (86400) + System.currentTimeMillis())); // default expiry for 20 days;

        url = save(EncodeUtil.Base62.encode(keyCounterService.getNextKeyCount()), url);

        if (url == null)
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "Failed to create url");

        if (ObjUtil.isBlank(url.getCreatedBy()))
            return url.getId();

        UrlCreatedInDescByUser urlCreatedInDescByUser = save(new UrlCreatedInDescByUser(url.getCreatedBy(), url.getCreatedAt(), url.getId()));
        if (urlCreatedInDescByUser == null) {
            delete(url.getId());
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "Failed to create url");
        }

        return url.getId();
    }


    @CachePut(value = "url", key = "#id")
    @Override
    public Url save(String id, Url url) {

        Preconditions.checkArgument(url == null, "Invalid url to save");
        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid url id to save");

        url.setId(id);
        long time = System.currentTimeMillis();
        if(url.getCreatedAt() <= 0)
            url.setCreatedAt(time);
        url.setModifiedAt(time);

        try {
            return urlRepository.save(url);
        } catch (Exception e) {
            log.error("Exception while saving url :" , e.getMessage(), e);
            return null;
        }
    }


    @Cacheable(value = "url", key = "#id")
    @Override
    public Url get(String id) {
        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid urlId to fetch url");
        Optional<Url> optional = Optional.empty();
        try {
            optional = urlRepository.findById(id);
        } catch (Exception e) {
           log.error("Exception while fetching url :" ,  e.getMessage(), e);
        }
        return optional.orElse(null);
    }

    @CacheEvict(value = "url", key = "#id")
    @Override
    public boolean delete(String id) {

        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid urlId to delete url");
        try {
            urlRepository.deleteById(id);
        } catch (Exception e) {
           log.error("Exception while deleting url by Id:", e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public List<Url> getByCreatedAtDesc(String createdBy, long createdAt) {

        Preconditions.checkArgument(ObjUtil.isBlank(createdBy), "Invalid userId to get urls");

        if (createdAt <= 0)
            createdAt = System.currentTimeMillis();

        List<UrlCreatedInDescByUser> urlCreatedInDescByUsers = getByUserIdAndCreatedAt(createdBy, createdAt);
        if(ObjUtil.isNullOrEmpty(urlCreatedInDescByUsers))
            return new ArrayList<>();

        Set<String> urlIds =  urlCreatedInDescByUsers.stream().map(UrlCreatedInDescByUser::getUrlId).collect(Collectors.toSet());
        return getByIds(urlIds);
    }

    @Override
    public List<Url> getByIds(Set<String> ids) {
        return getAllByIds(ids);
    }


    private List<Url> getAllByIds(Set<String> ids){
        try{
            return urlRepository.findAllById(ids);
        }catch (Exception e){
           log.error("Exception while fetching all urls by ids :", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    private List<UrlCreatedInDescByUser> getByUserIdAndCreatedAt(String userId, long createdAt) {
        try {
           return urlCreatedInDescByUserRepository.findByCreatedByAndCreatedAtLessThan(userId, createdAt);
        }catch (Exception e){
            log.error("Exception while fetching urlCreatedInDescByUser with userId and createdAt", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private UrlCreatedInDescByUser save(UrlCreatedInDescByUser urlCreatedInDescByUser) {
        try {
            return urlCreatedInDescByUserRepository.save(urlCreatedInDescByUser);
        } catch (Exception e) {
             log.error("Exception while creating urlCreatedInDescByUser : " , e.getMessage(), e);
            return null;
        }
    }
}
