package com.sflpro.notifier.persistence.dbmigration;

import org.flywaydb.core.internal.util.jdbc.DriverDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
public class MsNotificationFlywayStandaloneRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsNotificationFlywayStandaloneRunner.class);

    /* Constructors */
    public MsNotificationFlywayStandaloneRunner() {
        // Default constructor
    }

    /* Main method */
    public static void main(final String[] args) {
        LOGGER.info("Migration runner started");
        final String configPath = System.getProperty("flyway.config");
        Properties properties = null;
        try
        {
            Assert.isTrue(StringUtils.hasText(configPath) && new File(configPath).exists(), "configuration file was not set");
            properties = loadProperties();
        }
        catch (IllegalArgumentException ex)
        {
            LOGGER.error("Not all properties were configured correctly; {}", ex.getMessage());
            System.exit(1);
        }
        catch (IOException ex)
        {
            LOGGER.error("Configuration file could not be loaded; {}", ex.getMessage());
            System.exit(1);
        }

        try
        {
            final String driverClass = properties.getProperty("flyway.driver");
            final String url = properties.getProperty("flyway.url");
            final String user = properties.getProperty("flyway.user");
            final String password = properties.getProperty("flyway.password");
            final String action = properties.getProperty("flyway.action");
            final boolean validateOnMigrate = Boolean.parseBoolean(properties.getProperty("flyway.validateOnMigrate"));
            final boolean outOfOrder = Boolean.parseBoolean(properties.getProperty("flyway.outOfOrder"));

            final DataSource ds = new DriverDataSource(MsNotificationFlywayStandaloneRunner.class.getClassLoader(), driverClass, url, user, password);

            final MsNotificationFlyway flyway = new MsNotificationFlyway();
            flyway.setDataSource(ds);
            flyway.setDatabaseAction(action);
            flyway.setOutOfOrder(outOfOrder);
            flyway.setValidateOnMigrate(validateOnMigrate);
            flyway.startup();
            LOGGER.info("Migration performed successfully");
            System.exit(0);
        }
        catch (Exception ex)
        {
            LOGGER.error("An error occurred running the migration", ex);
            System.exit(1);
        }
    }

    /* Utility methods */
    public static Properties loadProperties() throws IOException {
        final Properties properties = new Properties();
        final String path = System.getProperty("flyway.config");
        if (StringUtils.hasText(path) && new File(path).exists()) {
            properties.load(new FileReader(path));
            return properties;
        }
        else {
            throw new IOException("Flyway migration configuration file could not be loaded");
        }
    }
}
