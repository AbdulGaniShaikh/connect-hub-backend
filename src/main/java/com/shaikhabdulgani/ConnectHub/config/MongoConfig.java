package com.shaikhabdulgani.ConnectHub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

/**
 * The `MongoConfig` class provides configuration for MongoDB transactions
 * in the context of a social media application.
 */
@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration {

    /**
     * Configures the MongoTransactionManager bean for handling MongoDB transactions.
     *
     * @param dbFactory The MongoDatabaseFactory instance.
     * @return The configured MongoTransactionManager bean.
     */
    @Bean
    MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
        return new MongoTransactionManager(dbFactory);
    }

    /**
     * Specifies the name of the MongoDB database.
     *
     * @return The name of the MongoDB database ("social-media" in this case).
     */
    @Override
    protected String getDatabaseName() {
        return "social-media";
    }


}
