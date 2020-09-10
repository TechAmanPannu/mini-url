package com.khalid;

import com.datastax.oss.driver.api.core.auth.AuthProvider;
import com.datastax.oss.driver.internal.core.auth.PlainTextAuthProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

import org.springframework.data.cassandra.config.SchemaAction;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Autowired
    private Environment env;

    @Override
    protected String getKeyspaceName() {
        return "tinyurl";
    }


    @Override
    protected String getContactPoints() {
        return env.getProperty("");
    }

    @Override
    protected int getPort() {
        return Integer.valueOf(env.getProperty(""));
    }

    @Override
    public SchemaAction getSchemaAction() {
        return SchemaAction.valueOf(env.getProperty(""));
    }


}