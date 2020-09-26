package com.miniurl.zookeeper.keycounter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Range implements Serializable {
   
    private static final long serialVersionUID = 1821698864716699855L;

    private long start;
    private long end;
    
}
