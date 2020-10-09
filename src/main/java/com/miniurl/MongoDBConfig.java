package com.miniurl;

import com.miniurl.constants.AppConstants;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.ServerAddress;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
@PropertySource({"mongodb-application.properties"})
public class MongoDBConfig {

    @Autowired
    private Environment env;

    @Bean
    public Datastore datastore(){


        log.info("connecting mongodb ..");
        Morphia morphia = new Morphia();
        morphia.mapPackage("com.miniurl.entity");

        MongoClientOptions.Builder options = MongoClientOptions.builder()
                .connectionsPerHost(1)
                .maxConnectionIdleTime((60 * 1_000))
                .maxConnectionLifeTime((120 * 1_000));
        MongoClientURI uri = new MongoClientURI("mongodb://mini-url-mongodb-0.mini-url-mongodb-headless.default.svc.cluster.local:27017,mini-url-mongodb-1.mini-url-mongodb-headless.default.svc.cluster.local:27017/?replicaSet=rs0", options);
        MongoClient mongo = new MongoClient(uri);

        Datastore datastore = morphia.createDatastore(mongo, "mini_url");
        datastore.ensureIndexes();
        datastore.ensureCaps();
        return datastore;
    }
}
