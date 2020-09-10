package com.miniUrl.repositories;

import com.miniUrl.entity.Contact;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends CassandraRepository<Contact, String> {

    @Override
    Optional<Contact> findById(String id);

    @Override
    Contact save(Contact entity);
}
