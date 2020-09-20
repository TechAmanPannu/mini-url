package com.miniurl.enums;

public enum AccessType {

    PUBLIC,

    PRIVATE;

    @Override
    public String toString() {
        return this.toString().toLowerCase();
    }
}
