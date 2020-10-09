package com.miniurl.entity;

import com.miniurl.enums.DomainConfigStatus;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Domain extends BaseEntity{
    private static final long serialVersionUID = 5594379037275478654L;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private DomainConfigStatus status;

    public Domain(String domain){
        id = domain;
    }
}
