package com.miniurl.dao;

import com.miniurl.entity.Url;
import com.miniurl.entity.indexes.url.UrlCreatedInDescByUser;
import com.miniurl.entity.indexes.url.UrlExpiresAtWithCreatedUser;
import com.miniurl.exception.EntityException;
import com.miniurl.model.request.UrlRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UrlDao {

    Url save(String id, Url url);

    Url get(String id);

    Url create(UrlRequest url) throws EntityException;

    List<Url> createBulk(UrlRequest url) throws EntityException;

    boolean delete(String id);

    List<Url> getByCreatedAtDesc(String createdBy, long createdAt);

    List<Url> getByIds(Set<String> ids);

    List<Url> getExpiredUrls(String createdBy, long byTime);

    UrlCreatedInDescByUser save(UrlCreatedInDescByUser urlCreatedInDescByUser);

    UrlExpiresAtWithCreatedUser save(UrlExpiresAtWithCreatedUser urlExpiresAtWithCreatedUser);
}
