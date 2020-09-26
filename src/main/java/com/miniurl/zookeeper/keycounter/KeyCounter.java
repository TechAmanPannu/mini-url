package com.miniurl.zookeeper.keycounter;

import com.miniurl.KafkaConfig;
import com.miniurl.utils.ObjUtil;
import com.miniurl.zookeeper.keycounter.model.Counter;
import com.miniurl.zookeeper.leaderselector.LeaderSelector;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.x.async.AsyncCuratorFramework;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class KeyCounter {

    static final String RANGE_CLUSTER = "/key_ranges";

    static final String SUB_RANGE = RANGE_CLUSTER + "/range";

    private static final long SUB_RANGE_LIMIT = 1000L;

    private CuratorFramework curatorFramework;

    private Counter counter;

    public KeyCounter(final CuratorFramework curatorFramework) {
        this.curatorFramework = curatorFramework;
        start();
    }

    public void start() {
        addCluster();
    }

    @SneakyThrows
    public boolean addCluster() {

        Stat stat = curatorFramework.checkExists().forPath(RANGE_CLUSTER);
        if (stat != null)
            return true;
        curatorFramework.create().creatingParentsIfNeeded()
                .forPath(RANGE_CLUSTER);

        return true;
    }

    @SneakyThrows
    public void addAllRanges() {

        long start = 100000L;
        long end = 3500000000000L;
        List<String> ranges = new ArrayList<>();
        int k = 1;
        int rangeNo = 1;
        for (long i = start ; i <= end ;i = i + 100000L  ){

            if(k == SUB_RANGE_LIMIT){

                log.info("creating sub range : "+rangeNo);
                curatorFramework.create()
                        .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                        .forPath(SUB_RANGE, ObjUtil.getJsonAsBytes(ranges));
                ranges = new ArrayList<>();
                k = 1;
                rangeNo ++;
            }
            long upto = i + 100000L;
            ranges.add(i + ":" + upto);
            k++;
        }

    }

}
