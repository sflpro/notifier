package com.sflpro.notifier.services.migration;

/**
 * User: Syuzanna Eprikyan
 * Company: SFL LLC
 * Date: 5/10/19
 * Time: 11:27 AM
 */
public interface FlywayMigration {
    void migrate(final String driver);
}
