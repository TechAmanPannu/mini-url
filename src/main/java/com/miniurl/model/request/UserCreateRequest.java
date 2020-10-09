package com.miniurl.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserCreateRequest {

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String profilePic;

    public UserCreateRequest(String email, String password, String firstName, String lastName, String profilePic){
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profilePic = profilePic;
    }

}
