package com.miniurl.zookeeper.keycounter.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Counter implements Serializable {

    private static final long serialVersionUID = 6553881070889437844L;

    private long count;

    private Range range;

}
