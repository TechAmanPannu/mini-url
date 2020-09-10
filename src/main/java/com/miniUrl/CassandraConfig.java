package com.miniUrl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;
import org.springframework.data.cassandra.core.cql.keyspace.KeyspaceOption;

import java.util.Collections;
import java.util.List;

@Configuration
@PropertySource({ "classpath:${envTarget:dev}-application.properties" })
public class CassandraConfig extends AbstractCassandraConfiguration {

    private static final String KEYSPACE =  "\"mini_url\"";
    @Autowired
    private Environment env;

    @Override
    protected String getKeyspaceName() {
        return KEYSPACE;
    }


    @Override
    protected String getContactPoints() {
        return env.getProperty("spring.data.cassandra.contact-points");
    }

    @Override
    protected int getPort() {
        return Integer.valueOf(env.getProperty("spring.data.cassandra.port"));
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(env.getProperty("spring.data.cassandra.scheme"));
    }

    @Override
    protected String getLocalDataCenter() {
        return env.getProperty("spring.data.cassandra.datacenter");
    }

    @Override
    protected String getSessionName() {
        return env.getProperty("spring.data.cassandra.cluster.name");
    }


    @Override
    protected List<CreateKeyspaceSpecification> getKeyspaceCreations() {

        return Collections.singletonList(CreateKeyspaceSpecification.createKeyspace(KEYSPACE)
                .ifNotExists()
                .with(KeyspaceOption.DURABLE_WRITES, true)
                .withSimpleReplication());
    }


}