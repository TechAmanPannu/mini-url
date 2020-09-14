package com.miniurl.endpoints;


import com.miniurl.dao.ContactDao;
import com.miniurl.dao.UrlDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppEndpoint extends BaseEndpoint{

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private UrlDao urlDao;

}
