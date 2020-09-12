package com.miniurl.endpoints;


import com.miniurl.dao.ContactDao;
import com.miniurl.dao.UrlDao;
import com.miniurl.entity.contact.Contact;
import com.miniurl.entity.url.Url;
import com.miniurl.exception.EntityException;
import com.miniurl.model.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppEndpoint extends BaseEndpoint{

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private UrlDao urlDao;

}
