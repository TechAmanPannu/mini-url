package com.miniurl;

import com.miniurl.zookeeper.keycounter.KeyCounter;
import com.miniurl.zookeeper.leaderselector.LeaderSelectorConnection;
import com.miniurl.zookeeper.leaderselector.LeaderSelector;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.async.AsyncCuratorFramework;
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
    public AsyncCuratorFramework asyncCuratorFramework(){
       return  AsyncCuratorFramework.wrap(zookeeperFrameworkConfig());
    }
    @Bean
    public LeaderSelector leaderSelector(){
        LeaderSelector leaderSelector = new LeaderSelector(zookeeperFrameworkConfig());
        new LeaderSelectorConnection(zookeeperFrameworkConfig(), leaderSelector);
        return leaderSelector;
    }


    @Bean
    public KeyCounter keyCounter(){
        return new KeyCounter(asyncCuratorFramework());
    }

}
