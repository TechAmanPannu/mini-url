package com.miniurl.model.request;

import com.miniurl.enums.RoleResourceType;
import com.miniurl.enums.RoleType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleCreateRequest {

    private String userId;

    private RoleResourceType type;

    private String typeId;

    private RoleType role;


}
