package com.miniurl.repositories.UrlKeyCounter;

import com.miniurl.entity.UrlKeyCounter;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlKeyCounterRepository extends CassandraRepository<UrlKeyCounter, String> {

    @Query("update url_key_counter SET counter = counter+1 WHERE id = ?0 ")
    UrlKeyCounter updateCounterValue(String id);

}
