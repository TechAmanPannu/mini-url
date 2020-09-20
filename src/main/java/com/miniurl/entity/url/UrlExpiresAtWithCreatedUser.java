package com.miniurl.entity.url;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "url_expires_at_with_created_user")
public class UrlExpiresAtWithCreatedUser implements Serializable {

    private static final long serialVersionUID = 3799027170019750002L;

    @PrimaryKeyColumn(value = "created_by", type = PrimaryKeyType.PARTITIONED)
    private String createdBy;

    @PrimaryKeyColumn(value = "expires_at", type = PrimaryKeyType.CLUSTERED, ordinal = 0, ordering = Ordering.DESCENDING)
    private long expiresAt;

    @Column(value = "url_Id")
    private String urlId;

}

