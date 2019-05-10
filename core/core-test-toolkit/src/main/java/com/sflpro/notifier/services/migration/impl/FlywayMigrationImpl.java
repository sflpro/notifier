package com.sflpro.notifier.services.migration.impl;

import com.sflpro.notifier.services.migration.FlywayMigration;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 5/10/19
 * Time: 11:28 AM
 */

@Component
public class FlywayMigrationImpl implements FlywayMigration {

    private String driver;

    @Override
    public void migrate(final String driver) {
        final Flyway flyway = new Flyway();
        flyway.setDataSource(System.getProperty("database.url"),
                System.getProperty("database.username"),
                System.getProperty("database.password")
        );
        flyway.configure(getProperties(driver));
        flyway.migrate();
    }

    private Properties getProperties(final String driver) {
        Properties properties = new Properties();
        properties.setProperty("flyway.locations", "db/migration/" + driver);
        return properties;
    }
}
