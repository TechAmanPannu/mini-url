package com.miniurl.model.request;

import com.miniurl.enums.DomainConfigStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DomainUpdateRequest {

    private DomainConfigStatus status;
}
