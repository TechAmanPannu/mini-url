package com.miniUrl.endpoints;


import com.miniUrl.dao.ContactDao;
import com.miniUrl.entity.Contact;
import com.miniUrl.model.response.api.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.server.ExportException;

@RestController
public class AppEndpoint extends BaseEndpoint{

    @Autowired
    private ContactDao contactDao;

    @GetMapping(path = "/{userId}")
    public ApiResponse test(@PathVariable("userId") String userId) {

        ApiResponse response  = new ApiResponse();

        response.setOk(true);
        contactDao.save(new Contact(userId, "aman.pannu@gmail.com"));

        Contact contact = contactDao.getById(userId);
        response.add("contact", contact);
        return response;
    }
}
