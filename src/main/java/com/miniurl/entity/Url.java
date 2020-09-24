package com.miniurl.entity;


import com.miniurl.constants.AppConstants;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Url extends BaseEntity {

    private static final long serialVersionUID = 2385642147284863564L;

    @CassandraType(type = CassandraType.Name.TEXT)
    @Column(value = "created_by")
    private String createdBy;

    @CassandraType(type = CassandraType.Name.SET,  typeArguments = { CassandraType.Name.TEXT })
    private Set<String> users;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String url;

    @CassandraType(type = CassandraType.Name.TEXT) //todo need to convert it to enums
    @Column("access_type")
    private String accessType;

    @CassandraType(type = CassandraType.Name.BIGINT)
    @Column("expires_at")
    private Long expiresAt;

    public Url(String url){
        this.url = url;
    }

    public String constructMiniUrl() {
        Preconditions.checkArgument(ObjUtil.isBlank(id), "invalid id to construct mini url");

        StringBuilder sb = new StringBuilder(AppConstants.APP_URL);
        sb.append("/");
        sb.append(id);
        return sb.toString();
    }
}
