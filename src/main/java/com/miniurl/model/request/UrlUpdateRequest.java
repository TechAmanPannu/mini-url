package com.miniurl.model.request;

import com.miniurl.enums.UrlAccessType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UrlUpdateRequest {

    private UrlAccessType accessType;

    private Set<String> userIds;

    private String urlId;

}
