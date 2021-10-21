package com.app.candlesticks.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix = "mongodb")
@Profile("test")
@Primary
public class MongoConfigMock extends AbstractMongoClientConfiguration {

    private String databaseName;
    private String connectionStringValue;
    private String quotesCollection;

    @Override
    public MongoClient mongoClient() {
        return Mockito.mock(MongoClient.class);
    }

}