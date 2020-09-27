package com.miniurl.zookeeper.keycounter.model;

import lombok.Data;

import java.util.List;

@Data
public class Range {

    private List<SubRange> subRanges;

    private String rangeName;
}
