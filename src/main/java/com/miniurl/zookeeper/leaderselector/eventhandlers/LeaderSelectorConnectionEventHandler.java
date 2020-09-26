package com.miniurl.zookeeper.leaderselector.eventhandlers;

import org.springframework.stereotype.Component;

@Component
public interface LeaderSelectorConnectionEventHandler {
    void process();
}
