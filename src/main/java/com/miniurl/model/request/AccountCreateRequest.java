package com.miniurl.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountCreateRequest {

    private UserCreateRequest user;

    private String accountName;

}
