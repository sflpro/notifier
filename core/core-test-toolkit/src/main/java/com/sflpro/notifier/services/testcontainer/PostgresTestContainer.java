package com.sflpro.notifier.services.testcontainer;

import com.sflpro.notifier.services.migration.impl.FlywayMigrationImpl;
import org.junit.ClassRule;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 5/8/19
 * Time: 11:07 AM
 */

@Configuration
public class PostgresTestContainer extends PostgreSQLContainer{

    @ClassRule
    public static PostgreSQLContainer postgresContainer = new PostgreSQLContainer("postgres:9.6.8");

    static {
        postgresContainer.start();
        System.setProperty("database.hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
        System.setProperty("database.driver", postgresContainer.getDriverClassName());
        System.setProperty("database.url", postgresContainer.getJdbcUrl());
        System.setProperty("database.username", postgresContainer.getUsername());
        System.setProperty("database.password", postgresContainer.getPassword());
        new FlywayMigrationImpl().migrate("postgresql");
    }
}
