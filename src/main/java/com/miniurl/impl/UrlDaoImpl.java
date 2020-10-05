package com.miniurl.impl;

import com.miniurl.constants.AppConstants;
import com.miniurl.dao.UrlDao;
import com.miniurl.entity.Url;
import com.miniurl.entity.indexes.url.UrlCreatedInDescByUser;
import com.miniurl.entity.indexes.url.UrlExpiresAtWithCreatedUser;
import com.miniurl.exception.EntityException;
import com.miniurl.exception.enums.EntityErrorCode;
import com.miniurl.kafka.url.UrlProducer;
import com.miniurl.model.request.UrlRequest;
import com.miniurl.repositories.url.UrlCreatedInDescByUserRepository;
import com.miniurl.repositories.url.UrlExpiresAtWithCreatedUserRepository;
import com.miniurl.repositories.url.UrlRepository;
import com.miniurl.utils.EncodeUtil;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.zookeeper.keycounter.KeyCounter;
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
    private UrlExpiresAtWithCreatedUserRepository urlExpiresAtWithCreatedUserRepository;

    @Autowired
    private KeyCounter keyCounter;

    @Autowired
    private UrlProducer urlProducer;

    @Override
    public String create(UrlRequest urlRequest) throws EntityException {

        Preconditions.checkArgument(urlRequest == null, "Invalid url to save");
        Preconditions.checkArgument(ObjUtil.isBlank(urlRequest.getUrl()), "Invalid url string to create url");


        String createdBy = ObjUtil.isBlank(urlRequest.getUserId()) ? AppConstants.APP_USER : urlRequest.getUserId();

        Url url = new Url(urlRequest.getUrl());
        url.setAccessType("PUBLIC");
        url.setCreatedBy(createdBy);
        url.setExpiresAt((20 * (86400) + System.currentTimeMillis())); // default expiry for 20 days;
        url.setId(EncodeUtil.Base62.encode(keyCounter.getCountAndIncr()));

        url = save(url.getId(), url);

        if (url == null)
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "Failed to create url");

        if (ObjUtil.isBlank(url.getCreatedBy()))
            return url.getId();



        urlProducer.createUrl(url);
        return url.getId();
    }


    /// todo need to something to expire the the properties and need to test
    @CachePut(value = "url", key = "#id")
    @Override
    public Url save(String id, Url url) {

        Preconditions.checkArgument(url == null, "Invalid url to save");
        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid url id to save");

        url.setId(id);
        long time = System.currentTimeMillis();
        if (url.getCreatedAt() <= 0)
            url.setCreatedAt(time);
        url.setModifiedAt(time);

        try {
            return urlRepository.save(url);
        } catch (Exception e) {
            log.error("Exception while saving url :", e.getMessage(), e);
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
            log.error("Exception while fetching url :", e.getMessage(), e);
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
        if (ObjUtil.isNullOrEmpty(urlCreatedInDescByUsers))
            return new ArrayList<>();

        Set<String> urlIds = urlCreatedInDescByUsers.stream().map(UrlCreatedInDescByUser::getUrlId).collect(Collectors.toSet());
        return getByIds(urlIds);
    }

    @Override
    public List<Url> getByIds(Set<String> ids) {
        return getAllByIds(ids);
    }

    @Override
    public List<Url> getExpiredUrls(String createdBy, long byTime) {

        Preconditions.checkArgument(ObjUtil.isBlank(createdBy), "Invalid userId to get urls");

        if (byTime <= 0)
            byTime = System.currentTimeMillis();

        List<UrlExpiresAtWithCreatedUser> urlCreatedInDescByUsers = getByUserIdAndExpiredAt(createdBy, byTime);
        if (ObjUtil.isNullOrEmpty(urlCreatedInDescByUsers))
            return new ArrayList<>();

        Set<String> urlIds = urlCreatedInDescByUsers.stream().map(UrlExpiresAtWithCreatedUser::getUrlId).collect(Collectors.toSet());
        return getByIds(urlIds);

    }

    @Override
    public UrlCreatedInDescByUser save(UrlCreatedInDescByUser urlCreatedInDescByUser) {

        Preconditions.checkArgument(urlCreatedInDescByUser == null, "Invalid urlCreatedInDescByUser to save");
        Preconditions.checkArgument(ObjUtil.isBlank(urlCreatedInDescByUser.getUrlId()), "Invalid urlId to save urlCreatedInDescByUser");
        Preconditions.checkArgument(ObjUtil.isBlank(urlCreatedInDescByUser.getCreatedBy()), "Invalid createdBy to save urlCreatedInDescByUser");
        Preconditions.checkArgument(urlCreatedInDescByUser.getCreatedAt() <= 0, "Invalid createdAt to save urlCreatedInDescByUser");

        try {
            return urlCreatedInDescByUserRepository.save(urlCreatedInDescByUser);
        } catch (Exception e) {
            log.error("Exception while saving urlCreatedInDescByUser : ", e.getMessage(), e);
            return null;
        }
    }

    @Override
    public UrlExpiresAtWithCreatedUser save(UrlExpiresAtWithCreatedUser urlExpiresAtWithCreatedUser) {

        Preconditions.checkArgument(urlExpiresAtWithCreatedUser == null, "Invalid urlExpiresAtWithCreatedUser to save");
        Preconditions.checkArgument(ObjUtil.isBlank(urlExpiresAtWithCreatedUser.getCreatedBy()), "Invalid createdBy to save urlExpiresAtWithCreatedUser");
        Preconditions.checkArgument(ObjUtil.isBlank(urlExpiresAtWithCreatedUser.getUrlId()), "Invalid urlId to save urlExpiresAtWithCreatedUser");
        Preconditions.checkArgument(urlExpiresAtWithCreatedUser.getExpiresAt() <= 0, "Invalid expiresAt to save urlExpiresAtWithCreatedUser");
        try {
            return urlExpiresAtWithCreatedUserRepository.save(urlExpiresAtWithCreatedUser);
        } catch (Exception e) {
            log.error("Exception while saving UrlExpiresAtWithCreatedUser : ", e.getMessage(), e);
            return null;
        }
    }

    private List<UrlExpiresAtWithCreatedUser> getByUserIdAndExpiredAt(String userId, long byTime) {
        try {
            return urlExpiresAtWithCreatedUserRepository.findByCreatedByAndExpiresAtLessThan(userId, byTime);
        } catch (Exception e) {
            log.error("Exception while fetching urlCreatedInDescByUser with userId and expiresAt", e.getMessage(), e);
            return new ArrayList<>();
        }
    }


    private List<Url> getAllByIds(Set<String> ids) {
        try {
            return urlRepository.findAllById(ids);
        } catch (Exception e) {
            log.error("Exception while fetching all urls by ids :", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<UrlCreatedInDescByUser> getByUserIdAndCreatedAt(String userId, long createdAt) {
        try {
            return urlCreatedInDescByUserRepository.findByCreatedByAndCreatedAtLessThan(userId, createdAt);
        } catch (Exception e) {
            log.error("Exception while fetching urlCreatedInDescByUser with userId and createdAt", e.getMessage(), e);
            return new ArrayList<>();
        }
    }


}
