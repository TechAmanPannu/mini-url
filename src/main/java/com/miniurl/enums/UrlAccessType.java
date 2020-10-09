package com.miniurl.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UrlAccessType {

    PUBLIC("public"),

    PRIVATE("private");

    String value;

    UrlAccessType(String value){
        this.value = value;
    }

    @JsonCreator
    public static UrlAccessType fromValue(String value) {
        try {
            return UrlAccessType.valueOf(value.toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    public String getValue(){
        return this.value;
    }

}
