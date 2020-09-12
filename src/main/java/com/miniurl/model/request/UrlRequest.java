package com.miniurl.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.codehaus.jackson.annotate.JsonProperty;

@Data
@NoArgsConstructor
public class UrlRequest {

    @JsonProperty("url")
    private String url;
}
