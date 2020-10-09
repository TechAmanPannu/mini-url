package com.miniurl.endpoints;

import com.miniurl.constants.AppConstants;
import com.miniurl.dao.UrlDao;
import com.miniurl.entity.Url;
import com.miniurl.enums.UrlAccessType;
import com.miniurl.exception.EntityException;
import com.miniurl.exception.ForbiddenException;
import com.miniurl.model.request.UrlCreateRequest;
import com.miniurl.model.request.UrlUpdateRequest;
import com.miniurl.model.response.ApiResponse;
import com.miniurl.model.response.CollectionResponse;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
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
    public ApiResponse createUrl(@RequestBody UrlCreateRequest urlCreateRequest) throws EntityException {

        ApiResponse response = new ApiResponse();

        Preconditions.checkArgument(urlCreateRequest == null, "Invalid url request");
        Url url = urlDao.create(urlCreateRequest);

        response.setOk(true);
        response.add("url", url);
        return response;
    }


    @PostMapping(value = "/bulk")
    public ApiResponse createUrlInBulk(@RequestBody UrlCreateRequest urlCreateRequest) throws EntityException {

        ApiResponse response = new ApiResponse();

        Preconditions.checkArgument(urlCreateRequest == null, "Invalid url request");
        List<Url> urls = urlDao.createBulk(urlCreateRequest);

        response.setOk(true);
        response.add("urls", urls);
        return response;
    }

    @GetMapping("/user/{userId}")
    public ApiResponse getUserUrls(@PathVariable(value = "userId") String userId,
                                   @RequestParam(value = "access_type", required = false) String accessType,
                                   @RequestParam(value = "since", required = false) Long since,
                                   @RequestParam(value = "start_time", required = false) Long startTime,
                                   @RequestParam(value = "end_time", required = false) Long endTime,
                                   @RequestParam(value = "limit", required = false) Integer limit,
                                   @RequestParam(value = "offSet", required = false) Integer offSet) {

        ApiResponse response = new ApiResponse();

        UrlAccessType accessTypeEnum = UrlAccessType.fromValue(accessType);

        if (accessTypeEnum == null)
            accessTypeEnum = UrlAccessType.PUBLIC;

        CollectionResponse<Url> urlResponse = urlDao.getUserUrls(userId, accessTypeEnum, since, startTime, endTime, limit, offSet);

        response.add("urls", urlResponse.getItems());
        response.add("offSet", urlResponse.getOffSet());
        if (ObjUtil.isNullOrEmpty(urlResponse.getItems()))
            response.add("urls", new ArrayList<>());

        response.setOk(true);
        return response;
    }

    @DeleteMapping
    public ApiResponse deleteUserUrls(@RequestBody Set<String> urlIds) {

        ApiResponse response = new ApiResponse();

        urlDao.deleteUserUrls(AppConstants.APP_USER, urlIds);

        response.setOk(true);
        return response;
    }

    @PutMapping(value = "/{urlId}/access")
    public ApiResponse updateUrlAccess(@PathVariable("urlId") String urlId, @RequestBody UrlUpdateRequest urlUpdateRequest) throws ForbiddenException {

        ApiResponse response = new ApiResponse();

        Preconditions.checkArgument(urlUpdateRequest == null, "Invalid url update request");
        Url url = urlDao.updateUrlAccess(urlId, AppConstants.APP_USER, urlUpdateRequest);

        response.setOk(true);
        response.add("url", url);

        return response;
    }
}


