package com.miniurl.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Set;

@NoArgsConstructor
@Data
public class UrlRequest {

    @JsonProperty("url")
    private String url;

    @JsonProperty("userId")
    private String userId;

    private Set<String> urls;

    public UrlRequest(String url, String userId, Set<String> urls){
        this.url = url;
        this.userId = userId;
        this.urls = urls;
    }
}
