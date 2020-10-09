package com.miniurl.dao;

import com.miniurl.entity.User;
import com.miniurl.model.request.UserCreateRequest;
import org.springframework.stereotype.Service;

@Service
public interface UserDao {

    User create(UserCreateRequest userCreateRequest);

    User create(String email, String password, String firstName, String lastName, String profilePic);

    User save(User user);

    User get(String id);
}
