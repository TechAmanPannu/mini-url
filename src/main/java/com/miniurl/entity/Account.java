package com.miniurl.entity;

import com.miniurl.enums.EntityStatusType;
import com.miniurl.utils.ObjUtil;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.Reference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Account extends BaseEntity{
    private static final long serialVersionUID = 2959142539075671139L;

    private String name;

    @Indexed(options = @IndexOptions(unique = true, sparse = true, background = true))
    private List<Domain> domains;

    private String createdBy;

    private EntityStatusType status;

    public Account(String id, String name, String createdBy, EntityStatusType status){
        this.id = id;
        this.name = name;
        this.createdBy = createdBy;
        this.status = status;
    }

    public void addDomain(String domain){

        if(ObjUtil.isBlank(domain))
            return;

        if(ObjUtil.isNullOrEmpty(domains))
            domains = new ArrayList<>();

        domains.add(new Domain(domain));
    }

}
