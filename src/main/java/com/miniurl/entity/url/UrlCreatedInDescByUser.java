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
@Table(value = "url_created_in_desc_by_user")
public class UrlCreatedInDescByUser implements Serializable {

    private static final long serialVersionUID = 3330424151938847842L;

    @PrimaryKeyColumn(value = "created_by", type = PrimaryKeyType.PARTITIONED)
    private String createdBy;

    @PrimaryKeyColumn(value = "created_at", type = PrimaryKeyType.CLUSTERED, ordering = Ordering.DESCENDING)
    private long createdAt;

    @Column(value = "url_Id")
    private String urlId;

}

