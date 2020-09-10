package com.miniUrl.endpoints;



import com.miniUrl.entity.Contact;
import com.miniUrl.model.response.ApiResponse;
import com.miniUrl.repositories.ContactRepository;
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
    private ContactRepository contactRepository;

    @GetMapping(path = "/{urlId}")
    public ApiResponse test(@RequestParam(defaultValue = "", name = "urlId") String urlId, HttpServletResponse servletResponse) throws IOException {

        ApiResponse response  = new ApiResponse();

        response.setOk(true);
        contactRepository.save(new Contact("userId2", "amandeep.pannu@gmail.com"));

        Optional<Contact> optional = contactRepository.findById("userId");
        System.out.println("contact"+optional.get());
        servletResponse.sendRedirect("https://tinyurl.com");
        return response;
    }
}
