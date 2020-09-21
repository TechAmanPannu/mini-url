package com.miniurl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "url_key_counter")
public class UrlKeyCounter implements Serializable {


    private static final long serialVersionUID = 3365273568980290729L;

    @PrimaryKey
    private String id;

    @CassandraType(type = CassandraType.Name.COUNTER)
    protected long counter;

}