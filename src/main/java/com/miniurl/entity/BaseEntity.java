package com.miniurl.entity;

import dev.morphia.annotations.Id;
import dev.morphia.annotations.PreLoad;
import dev.morphia.annotations.PrePersist;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 3816491527885540126L;

    @Id
    protected String id;

    protected long createdAt;

    protected long modifiedAt;

    @PrePersist
    public void updateTimeStamp(){

        long time = System.currentTimeMillis();

        if(this.createdAt <= 0)
            this.createdAt = time;

        this.modifiedAt = time;
    }
}
