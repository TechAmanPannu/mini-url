package com.miniurl.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.miniurl.entity.Domain;
import com.miniurl.enums.UrlAccessType;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Data
public class UrlCreateRequest {

    @JsonProperty("url")
    private String url;

    @JsonProperty("createdBy")
    private String createdBy;

    private UrlAccessType accessType;

    private List<String> urls;

    private Set<String> userIds;

    private String domain;

    private String customLinkId;

    public UrlCreateRequest(String url, String createdBy, List<String> urls){
        this.url = url;
        this.createdBy = createdBy;
        this.urls = urls;
    }


}
