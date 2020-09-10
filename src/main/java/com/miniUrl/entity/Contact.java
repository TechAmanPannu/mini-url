package com.miniUrl.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@Table
public class Contact {

    @PrimaryKey
    private String Id;

    private String email;
}
