package com.miniurl.zookeeper;

import com.miniurl.constants.AppConstants;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LeaderSelectorService {

    private final static String PARENT = "/leader";

    private final static String CHILD = PARENT+ "/server";

    @Autowired
    private CuratorFramework curatorFramework;

    @SneakyThrows
    public void begin() {
        addParent();
        addChild();
        addWatchOnChild();
    }

    @SneakyThrows
    public boolean addParent(){

       Stat stat =  curatorFramework.checkExists().forPath(PARENT);
        if(stat != null)
            return true;

        String results = curatorFramework.create().creatingParentsIfNeeded()
                .forPath(PARENT);
        log.info("creating parent znode :"+results);
        return true;
    }

    @SneakyThrows
    public boolean addChild(){
        String results = curatorFramework.create()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(CHILD, AppConstants.SERVER_IP.getBytes());
        log.info("creating child : "+results);
        return true;
    }

    @SneakyThrows
    public boolean addWatchOnChild(){
        curatorFramework
                .getData()
                .watched()
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        log.info("watcher triggered ..");
                        log.info("watcher info : "+event.getType());
                    }
                })
                .forPath(CHILD);

        return true;

    }

    @SneakyThrows
    public List<String> getChildren(){
        return curatorFramework.getChildren()
                .forPath(PARENT);
    }




}
