package com.miniurl.zookeeper.leader;

import com.miniurl.exception.IllegalArgException;
import com.miniurl.zookeeper.leader.eventhandlers.LeaderSelectorConnectionEventHandler;
import com.miniurl.zookeeper.leader.eventhandlers.ServerNodeChangeEventHandler;
import com.miniurl.zookeeper.leader.eventhandlers.ServerNodeCreatedEventHandler;
import com.miniurl.zookeeper.leader.eventhandlers.ServerNodeDeletedEventHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;

import org.apache.curator.framework.recipes.cache.CuratorCacheListener;

@Slf4j
public class LeaderSelectorConnection {

    public LeaderSelectorConnection(final CuratorFramework client, final LeaderSelector leaderSelector ){

        CuratorCache curatorCache = CuratorCache.build(client, LeaderSelector.PARENT_PATH);
        curatorCache
                .listenable()
                .addListener(new CuratorCacheListener() {
                    @Override
                    public void event(Type type, ChildData oldData, ChildData data) {
                        log.info("Leader Selector Connection State");
                        LeaderSelectorConnectionEventHandler eventHandler = getConnectionStateHandler(type, oldData, data, leaderSelector);
                        eventHandler.process();
                    }
                });

        curatorCache.start();

    }

    private LeaderSelectorConnectionEventHandler getConnectionStateHandler(CuratorCacheListener.Type type, ChildData oldData, ChildData data, LeaderSelector leaderSelector) {

        switch (type){
            case NODE_CREATED: return new ServerNodeCreatedEventHandler(type, oldData, data, leaderSelector);
            case NODE_CHANGED: return new ServerNodeChangeEventHandler(type, oldData, data, leaderSelector);
            case NODE_DELETED:return new ServerNodeDeletedEventHandler(type, oldData, data, leaderSelector);
            default: throw new IllegalArgException("Invalid event type to handle");
        }
    }
}
