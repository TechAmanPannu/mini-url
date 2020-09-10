package com.miniUrl.endpoints;



import com.miniUrl.model.response.ApiResponse;
import com.miniUrl.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class AppEndpoint {

    @Autowired
    private ContactRepository contactRepository;

    @GetMapping(path = "/{urlId}")
    public ApiResponse test(@RequestParam(defaultValue = "", name = "urlId") String urlId, HttpServletResponse servletResponse) throws IOException {

        ApiResponse response  = new ApiResponse();

        response.setOk(true);
        contactRepository.findById(urlId);

        servletResponse.sendRedirect("https://tinyurl.com");
        return response;
    }
}
