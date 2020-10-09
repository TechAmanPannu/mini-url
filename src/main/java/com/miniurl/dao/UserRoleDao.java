package com.miniurl.dao;

import com.miniurl.entity.UserRole;
import com.miniurl.enums.RoleResourceType;
import com.miniurl.enums.RoleType;
import org.springframework.stereotype.Service;

@Service
public interface UserRoleDao {

    UserRole createRole(RoleResourceType type, String typeId, String userId, RoleType role);

    UserRole save(UserRole userRole);

    UserRole get(RoleResourceType type, String userId);
}
