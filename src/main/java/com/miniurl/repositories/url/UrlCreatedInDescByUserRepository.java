package com.miniurl.repositories.url;

import com.miniurl.entity.url.UrlCreatedInDescByUser;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UrlCreatedInDescByUserRepository extends CassandraRepository<UrlCreatedInDescByUser, String> {

    @Override
    UrlCreatedInDescByUser save(UrlCreatedInDescByUser urlCreatedInDescByUser);

    List<UrlCreatedInDescByUser> findByUserIdAndCreatedAt(String userId, long createdAt);
}
