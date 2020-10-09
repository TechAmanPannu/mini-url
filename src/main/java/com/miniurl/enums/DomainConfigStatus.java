package com.miniurl.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum  DomainConfigStatus {

     REQUESTED, PROCESSING, CONFIGURED, CANCELLED;

     @JsonCreator
     public static DomainConfigStatus fromValue(String value) {
          try {
               return DomainConfigStatus.valueOf(value.toUpperCase());
          } catch (Exception e) {
               return null;
          }
     }
}
