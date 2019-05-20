package com.sflpro.notifier.services.testcontainer;

import com.sflpro.notifier.services.migration.impl.FlywayMigrationImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 5/8/19
 * Time: 11:07 AM
 */

@Configuration
public class DatabaseTestContainer {

    static JdbcDatabaseContainer postgresContainer;

    static final String MYSQL_DRIVER = "mysql";

    static final String POSTGRESQL_DRIVER = "postgresql";

    static {
        final String dbDriver = System.getProperty("test.db.driver");
        if (StringUtils.isNotBlank(dbDriver)) {
            if (MYSQL_DRIVER.equals(dbDriver)) {
                postgresContainer = new MySQLContainer("mysql:5.6.44");
                System.setProperty("database.hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
            } else if (POSTGRESQL_DRIVER.equals(dbDriver)) {
                postgresContainer = new PostgreSQLContainer("postgres:9.6.8");
                System.setProperty("database.hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect");
            }
            postgresContainer.start();
            System.setProperty("database.driver", postgresContainer.getDriverClassName());
            System.setProperty("database.url", postgresContainer.getJdbcUrl());
            System.setProperty("database.username", postgresContainer.getUsername());
            System.setProperty("database.password", postgresContainer.getPassword());
            new FlywayMigrationImpl().migrate(dbDriver);
        }

    }

}
