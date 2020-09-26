package com.miniurl.zookeeper.leader;

import com.miniurl.constants.AppConstants;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.zookeeper.leader.model.ServerNode;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;;


import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Data
public class LeaderSelector {

    public final static String PARENT_PATH = "/leader";

    public final static String CHILD_PATH = PARENT_PATH + "/server";

    public final static String SERVER_PATH = PARENT_PATH + "/%s";

    private static CuratorFramework curatorFramework;

    private ServerNode server;

    public LeaderSelector(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
    }

    @SneakyThrows
    public void begin() {
        addCluster();
        addServer(AppConstants.SERVER);
        addServerListener();
    }

    @SneakyThrows
    public boolean addCluster() {

        Stat stat = curatorFramework.checkExists().forPath(PARENT_PATH);
        if (stat != null)
            return true;

        String results = curatorFramework.create().creatingParentsIfNeeded()
                .forPath(PARENT_PATH);
        log.info("creating parent znode :" + results);
        return true;
    }

    @SneakyThrows
    public boolean addServer(ServerNode server) {

        Preconditions.checkArgument(server == null, "Invalid server to create server node in zookeeper");
        Preconditions.checkArgument(ObjUtil.isBlank(server.getId()), "Invalid server id to create server node in zookeeper");
        Preconditions.checkArgument(ObjUtil.isBlank(server.getHost()), "Invalid server host to create server node in zookeeper");

        String results = curatorFramework.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(CHILD_PATH, ObjUtil.getJson(server).getBytes());
        log.info("creating child : " + results);
        return true;
    }

    @SneakyThrows
    public boolean addServerListener() {
        curatorFramework
                .getData()
                .watched()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        log.info("Server Event Listener..");
                        log.info("Event type : " + event.getType());
                    }
                })
                .forPath(CHILD_PATH);

        return true;

    }


    @SneakyThrows
    public List<String> getServerNames() {
        return curatorFramework.getChildren()
                .forPath(PARENT_PATH);
    }

    @SneakyThrows
    public ServerNode getServerNode(String name){

       return ObjUtil.safeConvertJson(new String(curatorFramework.getData()
                .forPath(String.format(SERVER_PATH, name)),StandardCharsets.UTF_8 ), ServerNode.class);
    }

}
