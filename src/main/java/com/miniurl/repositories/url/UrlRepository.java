package com.miniurl.repositories.url;

import com.miniurl.entity.url.Url;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends CassandraRepository<Url, String> {

    @Override
    Url save(Url url);

    @Override
    void deleteById(String urlId);

    Optional<Url> findById(String urlId);

    @Override
    List<Url> findAllById(Iterable<String> strings);
}
