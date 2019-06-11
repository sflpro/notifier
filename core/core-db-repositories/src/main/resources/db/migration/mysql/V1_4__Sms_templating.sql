alter table notification_sms add column template_name varchar(255);

create table if not exists notification_sms_property (
  id bigint not null auto_increment primary key,
  notification_id bigint not null,
  property_key varchar(255) not null,
  property_value varchar(255) not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  CONSTRAINT fk_notification_sms_property_notification_sms FOREIGN KEY (notification_id) REFERENCES notification_sms (id)
);