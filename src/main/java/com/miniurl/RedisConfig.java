package com.miniurl;

import io.lettuce.core.ReadFrom;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.List;

@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {

    private RedisInstance master;
    private List<RedisInstance> slaves;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        log.info("Connecting to redis host...");
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(this.getMaster().getHost(), this.getMaster().getPort());
        this.getSlaves().forEach(slave -> staticMasterReplicaConfiguration.addNode(slave.getHost(), slave.getPort()));
        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
    }

    @Data
    private static class RedisInstance {

        private String host;
        private int port;
    }
}
