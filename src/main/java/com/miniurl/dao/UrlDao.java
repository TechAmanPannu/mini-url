package com.miniurl.dao;

import com.miniurl.entity.Url;
import com.miniurl.enums.UrlAccessType;
import com.miniurl.exception.EntityException;
import com.miniurl.exception.ForbiddenException;
import com.miniurl.model.request.UrlCreateRequest;
import com.miniurl.model.request.UrlUpdateRequest;
import com.miniurl.model.response.CollectionResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

@Service
public interface UrlDao {

    Url save(String id, Url url);

    Url get(String domain, String linkId);

    Url create(UrlCreateRequest url) throws EntityException;

    List<Url> createBulk(UrlCreateRequest url) throws EntityException;

    CompletableFuture<List<Url>> createBulkAsync(UrlCreateRequest urlCreateRequest, ExecutorService executorService);

    boolean delete(String id);

    boolean delete(Set<String> ids);


    List<Url> getByIds(Set<String> ids);

    boolean deleteUserUrls(String appUrl, Set<String> urlIds);

    Url updateUrlAccess(String urlId, String userId, UrlUpdateRequest urlUpdateRequest) throws ForbiddenException;

    CollectionResponse<Url> getUserUrls(String userId, UrlAccessType accessType, Long since, Long startTime, Long endTime, Integer limit, Integer offSet);
}
