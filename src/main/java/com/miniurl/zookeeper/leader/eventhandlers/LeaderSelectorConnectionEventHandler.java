package com.miniurl.zookeeper.leader.eventhandlers;

import com.miniurl.zookeeper.leader.LeaderSelector;
import org.springframework.stereotype.Component;

@Component
public interface LeaderSelectorConnectionEventHandler {
    void process();
}
