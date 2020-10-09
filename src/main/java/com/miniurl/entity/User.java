package com.miniurl.entity;

import com.miniurl.entity.BaseEntity;
import com.miniurl.enums.EntityStatusType;
import com.miniurl.enums.UserStatusType;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.Reference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    private static final long serialVersionUID = 5944698829458544845L;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private String acctId;

    @Indexed(options = @IndexOptions(unique = true, sparse = true, background = true))
    private String email;

    private String firstName;

    private String lastName;

    private String profilePic;

    @Indexed
    private UserStatusType status;

    private String password;


    public User(String id, String email, String password, String firstName, String lastName, UserStatusType status, String profilePic, String acctId){
        this.id = id;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.status = status;
        this.acctId = acctId;
        this.profilePic = profilePic;
    }
}
