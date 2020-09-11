package com.miniUrl.repositories;

import com.miniUrl.entity.Contact;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends CassandraRepository<Contact, String> {

    @Override
    Optional<Contact> findById(final String id);

    @Override
    Contact save(final Contact entity);

    @Override
    Slice<Contact> findAll(final Pageable pageable);


}
