ALTER TABLE notification_email_third_party_property
    RENAME notification_email_property;
ALTER TABLE notification_email_property
    DROP FOREIGN KEY fk_notif_third_party_property_notif_third_party;
ALTER TABLE notification_email_property
    ADD CONSTRAINT fk_notif_email_property_notif_email
        FOREIGN KEY (notification_id)
            REFERENCES notification_email (id)
            ON DELETE CASCADE;

DROP TABLE notification_email_third_party;

UPDATE notification_email
SET type = 'EMAIL'
WHERE type = 'EMAIL_THIRD_PARTY';