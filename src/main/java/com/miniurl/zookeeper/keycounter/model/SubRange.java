package com.miniurl.zookeeper.keycounter.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.miniurl.utils.ObjUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class SubRange implements Serializable {

    private static final long serialVersionUID = 1821698864716699855L;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long start;

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private Long end;

    public SubRange(final long start, final long end) {
        this.start = start;
        this.end = end;
    }

    public static List<SubRange> asList(String json) {

        try {
            return ObjUtil.getJacksonMapper().readValue(json,
                    new TypeReference<ArrayList<SubRange>>() {
                    });
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
}

