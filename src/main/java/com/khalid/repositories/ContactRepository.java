package com.khalid.repositories;

import com.khalid.entity.Contact;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends CassandraRepository<Contact, String> {


}
