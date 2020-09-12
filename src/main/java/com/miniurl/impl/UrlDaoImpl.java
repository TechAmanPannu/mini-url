package com.miniurl.impl;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.url.Url;
import com.miniurl.entity.url.UrlCreatedInDescByUser;
import com.miniurl.exception.EntityException;
import com.miniurl.exception.enums.EntityErrorCode;
import com.miniurl.repositories.url.UrlRepository;
import com.miniurl.repositories.url.UrlCreatedInDescByUserRepository;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UrlDaoImpl implements UrlDao {

    @Autowired
    private UrlRepository urlRepository;

    @Autowired
    private UrlCreatedInDescByUserRepository urlCreatedInDescByUserRepository;

    @Override
    public Url create(Url url) throws EntityException {

        Preconditions.checkArgument(url == null, "Invalid url to save");
        System.out.println("url string : "+url.getUrl());
        Preconditions.checkArgument(ObjUtil.isBlank(url.getUrl()), "Invalid url string to create url");

        url.setAccessType("PUBLIC");
        url.setCreatedAt(System.currentTimeMillis());
        url.setId(RandomStringUtils.randomAlphabetic(8));
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
        try {
            return urlRepository.save(url);
        } catch (Exception e) {
            System.out.println("Exception while saving url :" + e.getMessage());
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
            System.out.println("Exception while fetching url :" + e.getMessage());
        }
        return optional.orElse(null);
    }

    @Override
    public boolean delete(String urlId) {

        Preconditions.checkArgument(ObjUtil.isBlank(urlId), "Invalid urlId to delete url");
        try {
            urlRepository.deleteById(urlId);
        } catch (Exception e) {
            System.out.println("Exception while deleting url by Id:" + e.getMessage());
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
            System.out.println("Exception while fetching all urls by ids :"+e.getMessage());
            return new ArrayList<>();
        }
    }
    private List<UrlCreatedInDescByUser> getByUserIdAndCreated(String userId, long createdAt) {
        try {
           return urlCreatedInDescByUserRepository.findByUserIdAndCreatedAt(userId, createdAt);
        }catch (Exception e){
            System.out.println("Exception while fetching urlCreatedInDescByUser with userId and createdAt"+ e.getMessage());
            return new ArrayList<>();
        }
    }

    private UrlCreatedInDescByUser save(UrlCreatedInDescByUser urlCreatedInDescByUser) {
        try {
            return urlCreatedInDescByUserRepository.save(urlCreatedInDescByUser);
        } catch (Exception e) {
            System.out.println("Exception while creating urlCreatedInDescByUser : " + e.getMessage());
            return null;
        }
    }
}
