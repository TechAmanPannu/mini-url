package com.miniurl.endpoints;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.url.Url;
import com.miniurl.exception.EntityException;
import com.miniurl.model.request.UrlRequest;
import com.miniurl.model.response.ApiResponse;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/url")
public class UrlEndpoint extends BaseEndpoint {

    @Autowired
    private UrlDao urlDao;

    @PostMapping
    public ApiResponse createUrl(UrlRequest urlRequest) throws EntityException {

        ApiResponse response = new ApiResponse();

        Preconditions.checkArgument(urlRequest == null, "Invalid url request");


        Url url = urlDao.create(new Url(urlRequest.getUrl()));

        response.setOk(true);
        response.add("url", url);

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

}
