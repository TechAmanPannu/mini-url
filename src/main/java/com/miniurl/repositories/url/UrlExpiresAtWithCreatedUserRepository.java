package com.miniurl.repositories.url;

import com.miniurl.entity.url.UrlCreatedInDescByUser;
import com.miniurl.entity.url.UrlExpiresAtWithCreatedUser;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlExpiresAtWithCreatedUserRepository extends CassandraRepository<UrlExpiresAtWithCreatedUser, String> {

    @Override
    UrlExpiresAtWithCreatedUser save(UrlExpiresAtWithCreatedUser urlExpiresAtWithCreatedUser);

    List<UrlExpiresAtWithCreatedUser> findByCreatedByAndExpiresAtLessThan(String createdBy, long expiresAt);
}
