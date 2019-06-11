alter table notification_sms add column if not exists template_name varchar(255);

create table if not exists notification_sms_property (
  id bigint not null constraint pk_notification_sms_property primary key,
  notification_id bigint not null constraint fk_notification_sms_property_notification_sms references notification_sms,
  property_key varchar(255) not null,
  property_value varchar(255) not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);