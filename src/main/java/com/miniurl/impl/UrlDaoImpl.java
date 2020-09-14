package com.miniurl.impl;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.url.Url;
import com.miniurl.entity.url.UrlCreatedInDescByUser;
import com.miniurl.exception.EntityException;
import com.miniurl.exception.enums.EntityErrorCode;
import com.miniurl.redis.RedisService;
import com.miniurl.repositories.url.UrlCreatedInDescByUserRepository;
import com.miniurl.repositories.url.UrlRepository;
import com.miniurl.utils.EncodeUtil;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private RedisService redisService;

    @Override
    public Url create(Url url) throws EntityException {

        Preconditions.checkArgument(url == null, "Invalid url to save");
        Preconditions.checkArgument(ObjUtil.isBlank(url.getUrl()), "Invalid url string to create url");

        url.setAccessType("PUBLIC");
        long time = System.currentTimeMillis();
        url.setCreatedAt(time);
        url.setModifiedAt(time);
        url.setId(EncodeUtil.Base62.encode(redisService.getNextCount()));
        url.setMiniUrl("Just checking .."); // todo need to remove
        url = save(url);

        if (url == null)
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "Failed to create url");

        if (ObjUtil.isBlank(url.getCreatedBy()))
            return url;

        UrlCreatedInDescByUser urlCreatedInDescByUser = save(new UrlCreatedInDescByUser(url.getCreatedBy(), url.getCreatedAt(), url.getId()));
        if (urlCreatedInDescByUser == null) {
            delete(url.getId());
            throw new EntityException(EntityErrorCode.CREATE_FAILED, "Failed to create url");
        }

        return url;
    }

    @Override
    public Url save(Url url) {

        Preconditions.checkArgument(url == null, "Invalid url to save");
        Preconditions.checkArgument(ObjUtil.isBlank(url.getId()), "Invalid url id to save");
        try {
            return urlRepository.save(url);
        } catch (Exception e) {
            log.error("Exception while saving url :" , e.getMessage(), e);
            return null;
        }
    }


    @Override
    public Url get(String urlId) {
        Preconditions.checkArgument(ObjUtil.isBlank(urlId), "Invalid urlId to fetch url");
        Optional<Url> optional = Optional.empty();
        try {
            optional = urlRepository.findById(urlId);
        } catch (Exception e) {
           log.error("Exception while fetching url :" ,  e.getMessage(), e);
        }
        return optional.orElse(null);
    }

    @Override
    public boolean delete(String urlId) {

        Preconditions.checkArgument(ObjUtil.isBlank(urlId), "Invalid urlId to delete url");
        try {
            urlRepository.deleteById(urlId);
        } catch (Exception e) {
           log.error("Exception while deleting url by Id:", e.getMessage(), e);
            return false;
        }
        return true;
    }

    @Override
    public List<Url> getByCreatedAtDesc(String userId, long createdAt) {

        Preconditions.checkArgument(ObjUtil.isBlank(userId), "Invalid userId to get urls");

        if (createdAt <= 0)
            createdAt = System.currentTimeMillis();

        List<UrlCreatedInDescByUser> urlCreatedInDescByUsers = getByUserIdAndCreated(userId, createdAt);
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
    private List<UrlCreatedInDescByUser> getByUserIdAndCreated(String userId, long createdAt) {
        try {
           return urlCreatedInDescByUserRepository.findByUserIdAndCreatedAt(userId, createdAt);
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
