package com.sflpro.notifier.persistence.dbmigration;

import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Company: SFL LLC
 * Created on 07/12/2017
 *
 * @author Davit Harutyunyan
 */
public class MsNotificationFlyway extends Flyway {
    private static final Logger LOGGER = LoggerFactory.getLogger(MsNotificationFlyway.class);

    /* Properties */
    private String databaseAction;

    public MsNotificationFlyway() {
        // Default constructor
    }

    /* Properties getters and setters */
    public void setDatabaseAction(final String databaseAction) {
        this.databaseAction = databaseAction;
    }

    /* Public methods */
    public int startup() throws FlywayException {
        final DatabaseAction action;
        if (StringUtils.isNotEmpty(databaseAction)) {
            action = DatabaseAction.valueOf(databaseAction.toUpperCase());
            LOGGER.debug("Database action set to '{}'",action);
        }
        else {
            action = null;
            LOGGER.debug("Database action was not set");
        }
        if (DatabaseAction.CREATE.equals(action)) {
            LOGGER.debug("Cleaning up database");
            clean();
            LOGGER.info("Database cleanup completed");
        }
        int migrationCode = 0;
        if (DatabaseAction.CREATE.equals(action) || DatabaseAction.UPDATE.equals(action)) {
            LOGGER.debug("Migrating database");
            migrationCode = migrate();
            LOGGER.info("Database migration completed, code is '{}'",migrationCode);
            return migrationCode;
        }
        return migrationCode;
    }

    private enum DatabaseAction {
        CREATE, UPDATE
    }
}
