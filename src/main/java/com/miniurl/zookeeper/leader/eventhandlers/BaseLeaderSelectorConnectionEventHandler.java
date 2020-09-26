package com.miniurl.zookeeper.leader.eventhandlers;

import com.miniurl.constants.AppConstants;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.zookeeper.leader.LeaderSelector;
import com.miniurl.zookeeper.leader.model.ServerNode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@NoArgsConstructor
@Component
public class BaseLeaderSelectorConnectionEventHandler implements LeaderSelectorConnectionEventHandler {

    private CuratorCacheListener.Type type;
    private ChildData oldData;
    private ChildData data;
    private LeaderSelector leaderSelector;

    public BaseLeaderSelectorConnectionEventHandler(CuratorCacheListener.Type type, ChildData oldData, ChildData data, LeaderSelector leaderSelector) {
        this.type = type;
        this.oldData = oldData;
        this.data = data;
        this.leaderSelector = leaderSelector;
    }

    @Override
    public void process() {

        Preconditions.checkArgument(type == null, "Invalid type to process server "+type+" state for leader selection");

        List<String> serverNames = leaderSelector.getServerNames();
        if(ObjUtil.isNullOrEmpty(serverNames))
            return;

        Collections.sort(serverNames);
        String leadServerName = serverNames.get(0);

        ServerNode leadServerNode = leaderSelector.getServerNode(leadServerName);

        if(leadServerNode == null)
            return;

        if(leadServerNode.getId().equals(AppConstants.SERVER.getId())) {
            log.info("Hey I am Lead server node and my Id is :"+AppConstants.SERVER.getId());
            AppConstants.SERVER.setLeader(true);
        }

    }
}
