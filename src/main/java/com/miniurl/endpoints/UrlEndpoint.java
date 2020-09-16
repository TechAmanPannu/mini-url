package com.miniurl.endpoints;

import com.miniurl.dao.UrlDao;
import com.miniurl.entity.url.Url;
import com.miniurl.exception.EntityException;
import com.miniurl.model.request.UrlRequest;
import com.miniurl.model.response.ApiResponse;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.utils.ServerUtils;
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
    public ApiResponse createUrl(@RequestBody UrlRequest urlRequest) throws EntityException {

        ApiResponse response = new ApiResponse();
        Preconditions.checkArgument(urlRequest == null, "Invalid url request");

        String miniUrl = urlDao.create(urlRequest);
        response.setOk(true);
        response.add("miniUrl", ServerUtils.getHost(httpRequest)+"/"+miniUrl);

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
