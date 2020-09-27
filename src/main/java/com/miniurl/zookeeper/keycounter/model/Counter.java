package com.miniurl.zookeeper.keycounter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Counter implements Serializable {

    private static final long serialVersionUID = 6553881070889437844L;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long count;

    private SubRange subRange;

    private String rangeName;

}
