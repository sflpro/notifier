create table if not exists device_user (
  id bigint not null primary key,
  uuid varchar(36) not null,
  os_type varchar(50) not null,
  nms_user_id bigint not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  unique key uk_device_user_nms_user_id_uuid (nms_user_id, uuid)
);

create table if not exists nms_user (
  id bigint not null primary key,
  uuid varchar(36) not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  unique key uk_nms_user_uuid (uuid)
);

alter table device_user add constraint fk_device_user_nms_user foreign key (nms_user_id) references nms_user(id) ;

create table if not exists notification (
  id bigint not null primary key,
  uuid varchar(36) not null,
  type varchar(31) not null,
  client_ip_address varchar(255),
  content varchar(20000),
  provider_external_uuid varchar(36),
  provider_type varchar(50) not null,
  state varchar(50) not null,
  subject varchar(255),
  created datetime not null,
  removed datetime,
  updated datetime not null,
  unique key uk_notification_uuid (uuid)
);

create table if not exists notification_email (
  id bigint not null primary key,
  recipient_email varchar(255) not null,
  sender_email varchar(255),
  template_name varchar(255),
  constraint fk_notification_email_notification foreign key(id) references notification(id)
);

create table if not exists notification_email_third_party (
  id bigint not null primary key,
   constraint fk_notification_email_third_party_notification_email foreign key(id) references notification_email(id)
);

create table if not exists notification_email_third_party_property (
  id bigint not null primary key,
  notification_id bigint not null,
  property_key varchar(255) not null,
  property_value varchar(4000),
  created datetime not null,
  removed datetime,
  updated datetime not null,
  constraint fk_notif_third_party_property_notif_third_party foreign key(notification_id) references notification_email_third_party(id)
);

create table if not exists notification_push (
  id bigint not null primary key,
  recipient_id bigint not null,
  constraint fk_notification_push_notification foreign key(id) references notification(id)
);

create table if not exists notification_push_property (
  id bigint not null primary key,
  push_notification_id bigint not null,
  property_key varchar(255) not null,
  property_value varchar(255) not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  constraint fk_notification_push_property_notification_push foreign key(push_notification_id) references notification_push(id)
);

create table if not exists notification_push_recipient (
  id bigint not null primary key,
  uuid varchar(36) not null,
  type varchar(31) not null,
  application_type varchar(50) not null,
  destination_route_token varchar(255) not null,
  device_operating_system_type varchar(255) not null,
  status varchar(50) not null,
  last_device_id bigint,
  subscription_id bigint not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  unique key uk_push_notification_recipient_type_tkn_subs_id_app_type  (type, destination_route_token, subscription_id, application_type),
  unique key uk_notoficiation_push_recipient_uuid (uuid),
  constraint fk_notification_push_recipeint_device_user foreign key(last_device_id) references device_user(id)
);

create index idx_push_notification_recipient_type on notification_push_recipient (type);

create index idx_push_notification_application_type on notification_push_recipient (application_type);

create index idx_push_notification_recipient_destination_route_token on notification_push_recipient (destination_route_token);

create index idx_push_notification_recipient_device_operating_system_type on notification_push_recipient (device_operating_system_type);

create index idx_push_notification_recipient_status on notification_push_recipient (status);

alter table notification_push add constraint fk_notification_push_notification_push_recipient foreign key(recipient_id) references notification_push_recipient(id);

create table if not exists notification_push_recipient_device (
  id bigint not null primary key,
  device_id bigint not null,
  recipient_id bigint not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  constraint uk_push_notification_recipient_device_device_recipient unique (device_id, recipient_id),
  constraint fk_notification_push_recipient_device_device_user foreign key(device_id) references device_user(id),
  constraint fk_push_recipient_device_notification_push_reciepeint foreign key(recipient_id) references notification_push_recipient(id)
);

create table if not exists notification_push_recipient_sns (
  id bigint not null
  primary key,
  platform_application_arn varchar(500) not null,
  constraint fk_push_recipient_sns_notification_push_recipient foreign key(id) references notification_push_recipient(id)
);

create table if not exists notification_push_subscription (
  id bigint not null primary key,
  nms_user_id bigint not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  unique key uk_notification_push_recipeint_sns_id(nms_user_id),
  constraint fk_notification_push_subscription_nms_user foreign key(nms_user_id) references nms_user(id)
);

alter table notification_push_recipient add constraint fk_notification_push_recipient_subscription foreign key (subscription_id) references notification_push_subscription(id);

create table if not exists notification_push_subscription_request (
  id bigint not null primary key,
  uuid varchar(36) not null,
  application_type varchar(50) not null,
  previous_subscription_request_uuid varchar(36),
  state varchar(50) not null,
  subscribe boolean not null,
  recipient_id bigint,
  nms_user_id bigint not null,
  user_device_id bigint not null,
  user_device_token varchar(2000) not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  unique key uk_notification_push_subscription_request (uuid),
  constraint fk_notification_push_subscription_request_recipient foreign key(recipient_id) references notification_push_recipient(id),
  constraint fk_notification_push_subscription_request_nms_user foreign key(nms_user_id) references nms_user(id),
  constraint fk_notification_push_subscription_request_device_user foreign key(user_device_id) references device_user(id)
);

create table if not exists notification_sms (
  id bigint not null primary key,
  recipient_mobile_number varchar(255) not null,
   constraint fk_notification_sms_notification foreign key(id) references notification(id)
);

create table if not exists notification_usernotification_sms (
  id bigint not null primary key,
  notification_id bigint not null,
  nms_user_id bigint not null,
  created datetime not null,
  removed datetime,
  updated datetime not null,
  unique key uk_notification_usernotification_sms_notification_id (notification_id),
  constraint fk_notification_usernotification_sms_notification foreign key(nms_user_id) references notification(id),
  constraint fk_notification_usernotification_sms_nms_user foreign key(nms_user_id) references nms_user(id)
);