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
@Table
public class UrlCreatedInDescByUser implements Serializable {

    private static final long serialVersionUID = 3330424151938847842L;

    @PrimaryKeyColumn(type = PrimaryKeyType.PARTITIONED)
    @Column(value = "user_id")
    private String userId;

    @PrimaryKeyColumn(type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    @Column(value = "created_at")
    private long createdAt;

    @Column(value = "url_Id")
    private String urlId;

}

