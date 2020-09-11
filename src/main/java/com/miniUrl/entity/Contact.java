package com.miniUrl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Contact implements Serializable {

    @PrimaryKey
    private String id;

    private String email;

}
