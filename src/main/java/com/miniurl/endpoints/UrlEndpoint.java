package com.miniurl.endpoints;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.Url;
import com.miniurl.exception.EntityException;
import com.miniurl.model.request.UrlRequest;
import com.miniurl.model.response.ApiResponse;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.utils.ServerUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/api/v1/url")
public class UrlEndpoint extends BaseEndpoint {

    @Autowired
    private UrlDao urlDao;


    @PostMapping
    public ApiResponse createUrl(@RequestBody UrlRequest urlRequest) throws EntityException {

        ApiResponse response = new ApiResponse();

        Preconditions.checkArgument(urlRequest == null, "Invalid url request");
        Url url = urlDao.create(urlRequest);

        response.setOk(true);
        response.add("url", url);
        return response;
    }


    @PostMapping(value = "/bulk")
    public ApiResponse createUrlInBulk(@RequestBody UrlRequest urlRequest) throws EntityException {

        ApiResponse response = new ApiResponse();

        Preconditions.checkArgument(urlRequest == null, "Invalid url request");
        List<Url> urls = urlDao.createBulk(urlRequest);

        response.setOk(true);
        response.add("urls", urls);
        return response;
    }

    @GetMapping("/user/{userId}")
    public ApiResponse getUserUrls(@PathVariable(value = "userId") String userId) {

        ApiResponse response = new ApiResponse();

        List<Url> urls = urlDao.getByCreatedAtDesc(userId, System.currentTimeMillis());

        response.add("urls", urls);

        if (ObjUtil.isNullOrEmpty(urls))
            response.add("urls", new ArrayList<>());

        response.setOk(true);
        return response;
    }

    @GetMapping("/user/{userId}/expired")
    public ApiResponse getExpiredUserUrls(@PathVariable(value = "userId") String userId) {

        ApiResponse response = new ApiResponse();

        List<Url> urls = urlDao.getExpiredUrls(userId, System.currentTimeMillis());

        response.add("urls", urls);

        if (ObjUtil.isNullOrEmpty(urls))
            response.add("urls", new ArrayList<>());

        response.setOk(true);
        return response;
    }

}
