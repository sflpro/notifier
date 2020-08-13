create table if not exists notification_property (
  id bigint not null constraint pk_notification_property primary key,
  notification_id bigint not null constraint fk_notification_property_notification references notification,
  property_key varchar(255) not null,
  property_value varchar(255) not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);


ALTER TABLE notification_email_property
    DROP CONSTRAINT fk_notif_email_property_notif_email;

DROP TABLE notification_email_property;

ALTER TABLE notification_push_property
    DROP CONSTRAINT fk_notification_push_property_notification_push;

DROP TABLE notification_push_property;

ALTER TABLE notification_property ALTER COLUMN property_value type varchar(65535);