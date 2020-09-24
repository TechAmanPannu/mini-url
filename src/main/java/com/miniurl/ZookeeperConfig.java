package com.miniurl;

import com.miniurl.zookeeper.LeaderSelectorService;
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

    @Bean
    public CuratorFramework zookeeperFrameworkConfig(){

        log.info("Connecting to Zookeeper Ensemble ");

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder().namespace("MiniUrl")
                .connectString(env.getProperty("zookeeper.connectString"))
                .retryPolicy(retryPolicy).build();
        client.start();
        return client;
    }



}
