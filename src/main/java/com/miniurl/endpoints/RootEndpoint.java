package com.miniurl.endpoints;

import com.miniurl.constants.AppConstants;
import com.miniurl.dao.UrlDao;
import com.miniurl.entity.Url;
import com.miniurl.enums.UrlAccessType;
import com.miniurl.exception.NotFoundException;
import com.miniurl.utils.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class RootEndpoint extends BaseEndpoint {

    @Autowired
    private UrlDao urlDao;

    @GetMapping(value = "/{urlId}")
    public void redirectToUrl(@PathVariable(value = "urlId") String urlId) throws IOException {

        String host = ServerUtil.getHost(httpRequest);
        String domain = null;
        if(!host.contains(AppConstants.APP_HOST))
            domain = host;

        log.info("host for redirection : "+domain);

        Url url = urlDao.get(domain, urlId);
        if (url == null)
            throw new NotFoundException("resource not found");

        UrlAccessType accessType  = url.getAccessType();
        if (accessType == UrlAccessType.PUBLIC)
            httpResponse.sendRedirect(url.getUrl());

        if(accessType == UrlAccessType.PRIVATE){
            // todo need to handle private url redirection with some check
            httpResponse.sendRedirect(url.getUrl());
        }
    }
}
