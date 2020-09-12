package com.miniurl.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseEntity {

    @PrimaryKey
    protected String id;

    @CassandraType(type = CassandraType.Name.BIGINT)
    protected long createdAt;

    @CassandraType(type = CassandraType.Name.BIGINT)
    protected long modifiedAt;
}
