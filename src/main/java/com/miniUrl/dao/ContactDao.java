package com.miniUrl.dao;

import com.miniUrl.entity.Contact;
import org.springframework.stereotype.Service;

@Service
public interface ContactDao {
    Contact save(Contact contact);

    Contact getById(String id);
}
