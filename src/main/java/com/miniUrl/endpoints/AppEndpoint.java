package com.miniUrl.endpoints;


import com.miniUrl.dao.ContactDao;
import com.miniUrl.entity.Contact;
import com.miniUrl.model.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@RestController
public class AppEndpoint {

    @Autowired
    private ContactDao contactDao;

    @GetMapping(path = "/{urlId}")
    public ApiResponse test(@RequestParam(defaultValue = "", name = "urlId") String urlId, HttpServletResponse servletResponse) throws IOException {

        ApiResponse response  = new ApiResponse();

        response.setOk(true);
        contactDao.save(new Contact("userId1", "aman.pannu@gmail.com"));

        Contact contact = contactDao.getById("userId1");
        response.add("contact", contact);
        return response;
    }
}
