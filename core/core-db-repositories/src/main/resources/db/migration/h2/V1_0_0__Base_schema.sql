create table if not exists device_user (
  id bigint not null constraint pk_device_user primary key,
  uuid varchar(36) not null,
  os_type varchar(50) not null,
  nms_user_id bigint not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);

CREATE UNIQUE INDEX pk_device_user ON device_user (nms_user_id, uuid);

create table if not exists nms_user (
  id bigint not null constraint pk_nms_user primary key,
  uuid varchar(36) not null constraint uk_nms_user_uuid unique,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);

alter table device_user add constraint fk_device_user_nms_user foreign key (nms_user_id) references nms_user ;

create table if not exists notification (
  id bigint not null constraint pk_notification primary key,
  uuid varchar(36) not null constraint uk_notification_uuid unique,
  type varchar(31) not null,
  client_ip_address varchar(255),
  content varchar(20000),
  provider_external_uuid varchar(36),
  provider_type varchar(50) not null,
  state varchar(50) not null,
  subject varchar(255),
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);

create table if not exists notification_email (
  id bigint not null constraint pk_notification_email primary key,
  recipient_email varchar(255) not null,
  sender_email varchar(255),
  template_name varchar(255),
  FOREIGN KEY (id) REFERENCES notification(id)
);

create table if not exists notification_email_third_party (
  id bigint not null constraint pk_notification_email_third_party primary key,
  foreign key (id) references notification_email(id)
);

create table if not exists notification_email_third_party_property (
  id bigint not null constraint pk_notification_email_third_party_property primary key,
  notification_id bigint not null,
  property_key varchar(255) not null,
  property_value varchar(4000),
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  foreign key (notification_id) references notification_email_third_party(id)
);

create table if not exists notification_push (
  id bigint not null constraint pk_notification_push primary key,
  recipient_id bigint not null,
  foreign key (id) references notification(id)
);

create table if not exists notification_push_property (
  id bigint not null constraint pk_notification_push_property primary key,
  push_notification_id bigint not null,
  property_key varchar(255) not null,
  property_value varchar(255) not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  foreign key(push_notification_id) REFERENCES notification_push(id)
);

create table if not exists notification_push_recipient (
  id bigint not null constraint pk_notification_push_recipient primary key,
  uuid varchar(36) not null constraint uk_notoficiation_push_recipient_uuid unique,
  type varchar(31) not null,
  application_type varchar(50) not null,
  destination_route_token varchar(255) not null,
  device_operating_system_type varchar(255) not null,
  status varchar(50) not null,
  last_device_id bigint,
  subscription_id bigint not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  constraint uk_push_notification_recipient_type_tkn_subs_id_app_type unique (type, destination_route_token, subscription_id, application_type),
  foreign key(last_device_id) references device_user(id)
);

create index if not exists idx_push_notification_recipient_type on notification_push_recipient (type);

create index if not exists idx_push_notification_application_type on notification_push_recipient (application_type);

create index if not exists idx_push_notification_recipient_destination_route_token on notification_push_recipient (destination_route_token);

create index if not exists idx_push_notification_recipient_device_operating_system_type on notification_push_recipient (device_operating_system_type);

create index if not exists idx_push_notification_recipient_status on notification_push_recipient (status);

alter table notification_push add constraint fk_notification_push_notification_push_recipient foreign key (recipient_id) references notification_push_recipient;

create table if not exists notification_push_recipient_device (
  id bigint not null constraint pk_notification_push_recipient_device primary key,
  device_id bigint not null constraint fk_notification_push_recipient_device_device_user  references device_user,
  recipient_id bigint not null constraint fk_notification_push_recipient_device_notification_push_reciepeint references notification_push_recipient,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  constraint uk_push_notification_recipient_device_device_recipient unique (device_id, recipient_id)
);

create table if not exists notification_push_recipient_sns (
  id bigint not null constraint notification_push_recipient_sns_pkey primary key,
  platform_application_arn varchar(500) not null,
  foreign key(id) references notification_push_recipient(id)
);

create table if not exists notification_push_subscription (
  id bigint not null constraint pk_notification_push_subscription primary key,
  nms_user_id bigint not null constraint uk_notification_push_recipeint_sns_ unique,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  foreign key(nms_user_id) references nms_user(id)
);

alter table notification_push_recipient add constraint fk_notification_push_recipient_subscription foreign key (subscription_id) references notification_push_subscription;

create table if not exists notification_push_subscription_request (
  id bigint not null constraint pk_notification_push_subscription_request primary key,
  uuid varchar(36) not null constraint uk_notification_push_subscription_request unique,
  application_type varchar(50) not null,
  previous_subscription_request_uuid varchar(36),
  state varchar(50) not null,
  subscribe boolean not null,
  recipient_id bigint,
  nms_user_id bigint not null,
  user_device_id bigint not null,
  user_device_token varchar(2000) not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  foreign key(recipient_id) references notification_push_recipient(id),
  foreign key(nms_user_id) references nms_user(id),
  foreign key(user_device_id) references device_user(id)
);

create table if not exists notification_sms (
  id bigint not null constraint pk_notification_sms primary key,
  recipient_mobile_number varchar(255) not null,
  foreign key(id) references notification(id)
);

create table if not exists notification_usernotification_sms (
  id bigint not null constraint pk_notification_user primary key,
  notification_id bigint not null constraint uk_notification_usernotification_sms_notification_id unique,
  nms_user_id bigint not null constraint fk_notification_usernotification_sms_nms_user references nms_user,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  foreign key(notification_id) references notification(id)
);