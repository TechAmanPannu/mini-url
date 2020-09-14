package com.miniurl.entity.url;


import com.miniurl.constants.CommonConstants;
import com.miniurl.entity.BaseEntity;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Table;
import java.io.Serializable;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Url extends BaseEntity implements Serializable {

    @CassandraType(type = CassandraType.Name.TEXT)
    private String createdBy;

    @CassandraType(type = CassandraType.Name.SET,  typeArguments = { CassandraType.Name.TEXT })
    private Set<String> users;

    @CassandraType(type = CassandraType.Name.TEXT)
    private String url;

    @Transient
    private String miniUrl;

    @CassandraType(type = CassandraType.Name.TEXT) //todo need to convert it to enums
    private String accessType;

    @CassandraType(type = CassandraType.Name.BIGINT)
    private long expiresAt;

    public Url(String url){
        this.url = url;
    }

    public String constructMiniUrl() {
        Preconditions.checkArgument(ObjUtil.isBlank(id), "invalid id to construct mini url");
        StringBuilder sb = new StringBuilder(CommonConstants.APP_URL);
        sb.append("/");
        sb.append(id);
        return sb.toString();
    }
}
