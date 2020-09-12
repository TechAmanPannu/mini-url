package com.miniurl.dao;

import com.miniurl.entity.url.Url;
import com.miniurl.exception.EntityException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface UrlDao {

    Url save(Url url);

    Url get(String urlId);

    Url create(Url url) throws EntityException;

    boolean delete(String urlId);

    List<Url> getByCreatedAtDesc(String userId, long createdAt);

    List<Url> getByIds(Set<String> ids);
}
