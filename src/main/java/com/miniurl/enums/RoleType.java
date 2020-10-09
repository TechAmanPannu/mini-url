package com.miniurl.enums;

public enum RoleType {

    MEMBER(3), ADMIN(2), OWNER(1);

    int priority;

    RoleType(int priority){
        this.priority = priority;
    }
}
