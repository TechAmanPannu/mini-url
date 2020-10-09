package com.miniurl.entity;


import com.fasterxml.jackson.core.type.TypeReference;
import com.miniurl.enums.UrlAccessType;
import com.miniurl.utils.HashUtil;
import com.miniurl.utils.ObjUtil;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity
public class Url extends BaseEntity implements Cloneable {

    private static final long serialVersionUID = 2385642147284863564L;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private String createdBy;

    private Set<String> users;

    private String url;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private UrlAccessType accessType;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private String acctId;

    @Indexed(options = @IndexOptions(sparse = true, background = true))
    private String domain;

    @Indexed(options = @IndexOptions(sparse = true))
    private String linkId;

    public Url(String url){
        this.url = url;
    }

    public static List<Url> asList(String json){

        try {
            return ObjUtil.getJacksonMapper().readValue(json,
                    new TypeReference<ArrayList<Url>>() {
                    });
        } catch (IOException e) {
            return null;
        }
    }

    public Url clone(){

        Url url = null;

        try {
            url = (Url) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return url;
    }


}
