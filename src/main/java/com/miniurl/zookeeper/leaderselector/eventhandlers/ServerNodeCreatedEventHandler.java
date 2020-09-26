package com.miniurl.zookeeper.leaderselector.eventhandlers;

import com.miniurl.zookeeper.leaderselector.LeaderSelector;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.springframework.stereotype.Component;


@Slf4j
@NoArgsConstructor
@Component
public class ServerNodeCreatedEventHandler extends BaseLeaderSelectorConnectionEventHandler {


    public ServerNodeCreatedEventHandler(CuratorCacheListener.Type type, ChildData oldData, ChildData data, LeaderSelector leaderSelector) {
        super(type, oldData, data, leaderSelector);
    }

    @Override
    public void process() {
        log.info("State Created");
        super.process();
    }
}
