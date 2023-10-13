package com.udsu.matcher.condiguration;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import static io.r2dbc.pool.PoolingConnectionFactoryProvider.MAX_SIZE;
import static io.r2dbc.spi.ConnectionFactoryOptions.*;

@Configuration
public class DatabaseClientConfig {
//    @Bean
//    public DatabaseClient databaseClient() {
//        ConnectionFactoryOptions connectionFactoryOptions = ConnectionFactoryOptions.builder()
//                .option(DRIVER, "pool")
//                .option(PROTOCOL, "postgresql")
//                .option(HOST, "localhost")
//                .option(USER, "role")
//                .option(PASSWORD, "wdw")
//                .option(DATABASE, "ldt_user")
//                .option(MAX_SIZE, 10)
//                .build();
//        return DatabaseClient.create(ConnectionFactories.get(connectionFactoryOptions));
//    }

//    @Bean
//    public DatabaseClient databaseClient1() {
//        ConnectionFactoryOptions connectionFactoryOptions = ConnectionFactoryOptions.builder()
//                .option(DRIVER, "pool")
//                .option(PROTOCOL, "postgresql")
//                .option(HOST, "10.2.0.10")
//                .option(USER, "irbis")
//                .option(PASSWORD, "irbis")
//                .option(DATABASE, "trademark")
//                .option(MAX_SIZE, 10)
//                .build();
//        return DatabaseClient.create(ConnectionFactories.get(connectionFactoryOptions));
//    }
}
