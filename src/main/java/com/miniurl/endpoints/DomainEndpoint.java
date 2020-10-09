package com.miniurl.endpoints;

import com.miniurl.dao.DomainDao;
import com.miniurl.dao.UserDao;
import com.miniurl.entity.Domain;
import com.miniurl.model.request.DomainCreateRequest;
import com.miniurl.model.request.DomainUpdateRequest;
import com.miniurl.model.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/domain")
public class DomainEndpoint extends BaseEndpoint {

    @Autowired
    private DomainDao domainDao;

    @PostMapping(value = "/acct/{acctId}")
    public ApiResponse requestDomain(@PathVariable(value = "acctId") String acctId, @RequestBody DomainCreateRequest domainCreateRequest) {

        ApiResponse response = new ApiResponse();

        Domain domain = domainDao.create(acctId, domainCreateRequest);

        response.setOk(true);
        response.add("domain", domain);
        return response;
    }

    @PutMapping(value = "/{domain}")
    public ApiResponse updateStatus(@PathVariable("domain") String domain, @RequestBody DomainUpdateRequest domainUpdateRequest) {

        ApiResponse response = new ApiResponse();

        Domain updateDomain = domainDao.update(domain, domainUpdateRequest.getStatus());

        response.setOk(true);
        response.add("domain", updateDomain);
        return response;
    }


}

