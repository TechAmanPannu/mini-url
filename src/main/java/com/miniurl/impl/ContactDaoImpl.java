package com.miniurl.impl;

import com.miniurl.dao.ContactDao;
import com.miniurl.entity.contact.Contact;
import com.miniurl.repositories.contact.ContactRepository;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import groovy.util.logging.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class ContactDaoImpl implements ContactDao {

    @Autowired
    ContactRepository contactRepository;

    @Override
    public Contact save(Contact contact) {

        Preconditions.checkArgument(contact == null, "Invalid contact to save");
        Preconditions.checkArgument(ObjUtil.isBlank(contact.getId()), "Invalid id to save contact");
        Preconditions.checkArgument(ObjUtil.isBlank(contact.getEmail()), "Invalid email to save contact");
        try { return contactRepository.save(contact); } catch (Exception e) { System.out.println("Exception while saving contact :"+e.getMessage());return null; } }

    @Override
    public Contact get(String id) {

        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid id to fetch contact");
        Optional<Contact> optional = Optional.empty();
        try { optional =  contactRepository.findById(id); }catch (Exception e){   System.out.println("Exception while fetching contact :"+e.getMessage()); }
        return optional.orElse(null);
    }
}
