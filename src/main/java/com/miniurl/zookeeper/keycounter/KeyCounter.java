package com.miniurl.zookeeper.keycounter;

import com.miniurl.KafkaConfig;
import com.miniurl.utils.ObjUtil;
import com.miniurl.zookeeper.keycounter.model.Counter;
import com.miniurl.zookeeper.leaderselector.LeaderSelector;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KeyCounter {

    static final String RANGE_CLUSTER = "/key_ranges";

    static final String SUB_RANGE = RANGE_CLUSTER + "/range";

    private static final long SUB_RANGE_LIMIT = 10000L;

    private AsyncCuratorFramework asyncCuratorFramework;

    private Counter counter;

    public KeyCounter(final AsyncCuratorFramework asyncCuratorFramework) {
        this.asyncCuratorFramework = asyncCuratorFramework;
        start();
    }

    public void start() {
        addCluster();
    }

    @SneakyThrows
    public boolean addCluster() {

        asyncCuratorFramework.checkExists().forPath(RANGE_CLUSTER)
                .thenAccept(stat -> {
                    if (stat != null) {
                        asyncCuratorFramework.create()
                                .forPath(RANGE_CLUSTER);
                    }
                });

        return true;
    }

    @SneakyThrows
    public void addAllRanges() {


//        if (!leaderSelector.getCurrentServer().isLeader()) {
//            log.info("Non Lead servers are not suppossed to create ranges.");
//            return;
//        }
//
//        List<String> range = new ArrayList<>();
//
//        int noOfRanges = 0;
//        do{
//            asyncCuratorFramework.create()
//                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
//                    .forPath(SUB_RANGE, ObjUtil.getJson(server).getBytes());
//        }while ()


    }
}
