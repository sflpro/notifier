CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_notification_property_notification_id
    ON notification_property USING btree
    (notification_id);
