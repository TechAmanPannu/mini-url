package com.miniurl.endpoints;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.url.Url;
import com.miniurl.exception.NotFoundException;
import com.miniurl.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class RootEndpoint extends BaseEndpoint{

    @Autowired
    private UrlDao urlDao;

    @GetMapping(value = "/{urlId}")
    public void redirectToUrl(@PathVariable(value = "urlId") String urlId) throws IOException {

        Url url = urlDao.get(urlId);
        if (url == null)
            throw new NotFoundException("resource not found");

        httpResponse.sendRedirect(url.getUrl());
    }
}
