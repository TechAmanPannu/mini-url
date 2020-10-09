package com.miniurl.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.miniurl.enums.EntityStatusType;
import com.miniurl.enums.RoleResourceType;
import com.miniurl.enums.RoleType;
import com.miniurl.utils.HashUtil;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class UserRole extends BaseEntity {
    private static final long serialVersionUID = -151996751899065218L;

    private RoleType role;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private RoleResourceType type;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private String typeId;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private String userId;

    private EntityStatusType status;

    public UserRole(String id, RoleResourceType type, RoleType role, String typeId, String userId, EntityStatusType status){
        this.id = id;
        this.type = type;
        this.role = role;
        this.typeId = typeId;
        this.userId = userId;
        this.status = status;

    }


}
