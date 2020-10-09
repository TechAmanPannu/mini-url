package com.miniurl.endpoints;

import com.miniurl.dao.AccountDao;
import com.miniurl.entity.Account;
import com.miniurl.entity.Url;
import com.miniurl.exception.EntityException;
import com.miniurl.model.request.AccountCreateRequest;
import com.miniurl.model.response.ApiResponse;
import com.miniurl.utils.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/account")
public class AccountEndpoint extends BaseEndpoint{

    @Autowired
    private AccountDao accountDao;

    @PostMapping(value = "/create")
    public ApiResponse createAccount(@RequestBody AccountCreateRequest accountCreateRequest) throws EntityException {

        ApiResponse response = new ApiResponse();

        Preconditions.checkArgument(accountCreateRequest == null, "Invalid account created request");
        Account account = accountDao.create(accountCreateRequest);

        response.setOk(true);
        response.add("account", account);
        return response;
    }


}
