package com.miniurl.entity.contact;

import com.miniurl.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Contact extends BaseEntity {

    private static final long serialVersionUID = 5944698829458544845L;

    private String email;


}
