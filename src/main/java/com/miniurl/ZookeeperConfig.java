package com.miniurl;

import com.miniurl.zookeeper.leader.LeaderSelectorConnection;
import com.miniurl.zookeeper.leader.LeaderSelector;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
@PropertySource({"zookeeper-application.properties"})
public class ZookeeperConfig {

    @Autowired
    private Environment env;

    @SneakyThrows
    @Bean
    public CuratorFramework zookeeperFrameworkConfig(){

        log.info("Connecting to Zookeeper Ensemble ");

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder().namespace("mini_url")
                .connectString(env.getProperty("zookeeper.connectString"))
                .retryPolicy(retryPolicy).build();

        client.start();
        client.getZookeeperClient().blockUntilConnectedOrTimedOut();
        return client;
    }

    @Bean
    public LeaderSelector leaderSelector(){
        LeaderSelector leaderSelector = new LeaderSelector(zookeeperFrameworkConfig());
        leaderSelector.begin();
        new LeaderSelectorConnection(zookeeperFrameworkConfig(), leaderSelector);
        return leaderSelector;
    }

}
