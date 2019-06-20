create table if not exists notification_property (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  notification_id bigint not null,
  property_key varchar(255) not null,
  property_value varchar(255) not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  CONSTRAINT fk_notification_property_notification FOREIGN KEY (notification_id) REFERENCES notification (id)
);

ALTER TABLE notification_email_property
    DROP FOREIGN KEY fk_notif_email_property_notif_email;

DROP TABLE notification_email_property;

ALTER TABLE notification_push_property
    DROP FOREIGN KEY fk_notification_push_property_notification_push;

DROP TABLE notification_push_property;

