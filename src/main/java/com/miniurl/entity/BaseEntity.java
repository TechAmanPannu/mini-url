package com.miniurl.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 3816491527885540126L;

    @PrimaryKey
    protected String id;

    @CassandraType(type = CassandraType.Name.BIGINT)
    protected long createdAt;

    @CassandraType(type = CassandraType.Name.BIGINT)
    protected long modifiedAt;
}
