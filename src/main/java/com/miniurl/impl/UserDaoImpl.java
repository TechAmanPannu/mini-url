package com.miniurl.impl;

import com.miniurl.dao.UserDao;
import com.miniurl.entity.User;
import com.miniurl.enums.UserStatusType;
import com.miniurl.model.request.UserCreateRequest;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import dev.morphia.Datastore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.UUID;

@Service
@Slf4j
public class UserDaoImpl implements UserDao {


    @Autowired
    private Datastore datastore;

    @Override
    public User create(UserCreateRequest userCreateRequest) {

        Preconditions.checkArgument(userCreateRequest == null, "Invalid user create request");
        Preconditions.checkArgument(ObjUtil.isBlank(userCreateRequest.getEmail()), "Invalid email to create user");
        Preconditions.checkArgument(ObjUtil.isBlank(userCreateRequest.getFirstName()), "Invalid firstname to created user");
        Preconditions.checkArgument(ObjUtil.isBlank(userCreateRequest.getPassword()), "Invalid password to create user");

        User existingUser = getByEmail(userCreateRequest.getEmail());
        Preconditions.checkArgument(existingUser != null, "user already exist with email : " + userCreateRequest.getEmail());

        User user = new User(UUID.randomUUID().toString(), userCreateRequest.getEmail(),
                userCreateRequest.getPassword(), userCreateRequest.getFirstName(), userCreateRequest.getLastName()
                , UserStatusType.ACTIVE, userCreateRequest.getProfilePic(), UUID.randomUUID().toString());

        return save(user);

    }

    @Override
    public User create(String email, String password, String firstName, String lastName, String profilePic) {
        return create(new UserCreateRequest(email, password, firstName, lastName, profilePic));
    }

    @Override
    public User save(User user) {
        return datastore.save(user) != null ? user : null;
    }

    @Override
    public User get(String id) {

        Preconditions.checkArgument(ObjUtil.isBlank(id), "Invalid id to get user");
        return datastore.createQuery(User.class)
                .field("id").equal(id).first();
    }

    public User getByEmail(String email) {

        Preconditions.checkArgument(ObjUtil.isBlank(email), "Invalid email to get user by email");
        return datastore.createQuery(User.class)
                .field("email").equal(email).first();
    }
}
