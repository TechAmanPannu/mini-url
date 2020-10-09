package com.miniurl.endpoints;


import com.miniurl.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserEndpoint extends BaseEndpoint{

    @Autowired
    private UserDao userDao;

//    @PostMapping(value = "/add/")
//    public ApiResponse addUserUnderAccount() throws EntityException {
//
//        ApiResponse response = new ApiResponse();
//
//        Preconditions.checkArgument(accountCreateRequest == null, "Invalid account created request");
//        Account account = userDao.addUserUnderAccount();
//
//        response.setOk(true);
//        response.add("account", account);
//        return response;
//    }


}

