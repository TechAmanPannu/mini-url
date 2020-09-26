package com.miniurl.zookeeper.leader.eventhandlers;


import com.miniurl.zookeeper.leader.LeaderSelector;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.springframework.stereotype.Component;

@Slf4j
@NoArgsConstructor
@Component
public class ServerNodeChangeEventHandler extends BaseLeaderSelectorConnectionEventHandler {

    public ServerNodeChangeEventHandler(CuratorCacheListener.Type type, ChildData oldData, ChildData data, LeaderSelector leaderSelector){

        super(type, oldData, data, leaderSelector);
    }

    @Override
    public void process() {
        log.info("State Change");
        super.process();
    }
}
