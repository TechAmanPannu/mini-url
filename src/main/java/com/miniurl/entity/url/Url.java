package com.miniurl.entity.url;


import com.datastax.oss.driver.api.core.type.DataType;
import com.miniurl.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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

    @CassandraType(type = CassandraType.Name.TEXT) //todo need to convert it to enums
    private String accessType;

    @CassandraType(type = CassandraType.Name.BIGINT)
    private long expiresAt;

    public Url(String url){
        this.url = url;
    }
}
