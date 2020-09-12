package com.miniurl.dao;

import com.miniurl.entity.contact.Contact;
import org.springframework.stereotype.Service;

@Service
public interface ContactDao {
    Contact save(Contact contact);

    Contact get(String id);
}
