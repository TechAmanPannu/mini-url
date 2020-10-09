package com.miniurl.impl;

import com.miniurl.dao.UserRoleDao;
import com.miniurl.entity.User;
import com.miniurl.entity.UserRole;
import com.miniurl.enums.EntityStatusType;
import com.miniurl.enums.RoleResourceType;
import com.miniurl.enums.RoleType;
import com.miniurl.utils.HashUtil;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import dev.morphia.Datastore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserRoleDaoImpl implements UserRoleDao {

    @Autowired
    private Datastore datastore;

    @Override
    public UserRole createRole(RoleResourceType type, String typeId, String userId, RoleType role) {

        Preconditions.checkArgument(ObjUtil.isBlank(typeId), "Invalid typeId to create user role");
        Preconditions.checkArgument(type == null, "Invalid type to create user role");
        Preconditions.checkArgument(ObjUtil.isBlank(userId), "Invalid userId to create user role");
        Preconditions.checkArgument(role == null, "Invalid role to create user role");

        UserRole existingRole = get(RoleResourceType.ACCOUNT, userId);
        Preconditions.checkArgument(existingRole != null, "user with userId :"+userId+" , is already part of an account");

        UserRole userRole = new UserRole(generateId(typeId, userId), type, role,
                typeId, userId, EntityStatusType.ACTIVE);

        return save(userRole);

    }


    @Override
    public UserRole save(UserRole userRole){

        return datastore.save(userRole) != null ? userRole : null;
    }
    @Override
    public UserRole get(RoleResourceType type, String userId){

        Preconditions.checkArgument(type == null, "Invalid type to get user role");
        Preconditions.checkArgument(ObjUtil.isBlank(userId), "Invalid userid to get user role");

       return datastore.createQuery(UserRole.class)
                .field("type").equal(type)
                .field("userId").equal(userId).first();

    }

    public static String generateId(String typeId, String userId) {
        return HashUtil.sha256("UserRole:"+typeId+":"+userId);
    }
}
