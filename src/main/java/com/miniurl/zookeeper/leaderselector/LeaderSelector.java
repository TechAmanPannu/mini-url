package com.miniurl.zookeeper.leaderselector;

import com.miniurl.constants.AppConstants;
import com.miniurl.utils.ObjUtil;
import com.miniurl.utils.Preconditions;
import com.miniurl.zookeeper.leaderselector.model.ServerNode;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Data
public class LeaderSelector {

    static final String CLUSTER = "/leader";

    static final String SERVER = CLUSTER + "/server";

    public static final String GET_SERVER = CLUSTER + "/%s";

    private CuratorFramework curatorFramework;

    private ServerNode currentServer;

    public LeaderSelector(CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
        this.currentServer = getServerFromEnv();
        start();
    }

    @SneakyThrows
    public void start() {
        addCluster();
        addServer();
        addServerListener();
    }

    @SneakyThrows
    public boolean addCluster() {

        Stat stat = curatorFramework.checkExists().forPath(CLUSTER);
        if (stat != null)
            return true;

        String results = curatorFramework.create().creatingParentsIfNeeded()
                .forPath(CLUSTER);

        return true;
    }

    @SneakyThrows
    public boolean addServer() {

        Preconditions.checkArgument(currentServer == null, "Invalid server to create server node in zookeeper");
        Preconditions.checkArgument(ObjUtil.isBlank(currentServer.getId()), "Invalid server id to create server node in zookeeper");
        Preconditions.checkArgument(ObjUtil.isBlank(currentServer.getHost()), "Invalid server host to create server node in zookeeper");

         curatorFramework.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(SERVER, ObjUtil.getJson(currentServer).getBytes());
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
                .forPath(SERVER);

        return true;

    }


    @SneakyThrows
    public List<String> getServerNames() {
        return curatorFramework.getChildren()
                .forPath(CLUSTER);
    }

    @SneakyThrows
    public ServerNode getServerNode(String name) {

        return ObjUtil.safeConvertJson(new String(curatorFramework.getData()
                .forPath(String.format(GET_SERVER, name)), StandardCharsets.UTF_8), ServerNode.class);
    }

    private static ServerNode getServerFromEnv() {

        final String podIp = System.getenv("POD_IP");
        final String  podId = System.getenv("POD_ID");

        Preconditions.checkArgument(ObjUtil.isBlank(podIp), "Invalid podId to start server");
        Preconditions.checkArgument(ObjUtil.isBlank(podId), "Invalid podId for running server");

        return new ServerNode(podId, podIp);
    }

}
