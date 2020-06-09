create sequence hibernate_sequence start 1 increment 50;

create table if not exists device_user (
  id bigint not null constraint pk_device_user primary key,
  uuid varchar(36) not null,
  os_type varchar(50) not null,
  nms_user_id bigint not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  constraint uk_device_user_nms_user_id_uuid unique (nms_user_id, uuid)
);

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
  id bigint not null constraint pk_notification_email primary key
    constraint fk_notification_email_notification references notification,
  recipient_email varchar(255) not null,
  sender_email varchar(255),
  template_name varchar(255)
);

create table if not exists notification_email_file_attachment (
  id bigint not null constraint pk_notification_email_file_attachment primary key,
  file_name varchar(255) not null,
  url varchar (1000) not null,
  mime_type varchar(255),
  notification_email_id bigint constraint fk_notification_email_file_attachment_notification_email references notification_email,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);

create table if not exists notification_email_third_party (
  id bigint not null constraint pk_notification_email_third_party
    primary key constraint fk_notification_email_third_party_notification_email references notification_email
);

create table if not exists notification_email_third_party_property (
  id bigint not null constraint pk_notification_email_third_party_property primary key,
  notification_id bigint not null constraint fk_notif_third_party_property_notif_third_party references notification_email_third_party,
  property_key varchar(255) not null,
  property_value varchar(4000),
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);

create table if not exists notification_push (
  id bigint not null constraint pk_notification_push primary key
    constraint fk_notification_push_notification references notification,
  recipient_id bigint not null
);

create table if not exists notification_push_property (
  id bigint not null constraint pk_notification_push_property primary key,
  push_notification_id bigint not null constraint fk_notification_push_property_notification_push references notification_push,
  property_key varchar(255) not null,
  property_value varchar(255) not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);

create table if not exists notification_push_recipient (
  id bigint not null constraint pk_notification_push_recipient primary key,
  uuid varchar(36) not null constraint uk_notoficiation_push_recipient_uuid unique,
  type varchar(31) not null,
  application_type varchar(50) not null,
  destination_route_token varchar(255) not null,
  device_operating_system_type varchar(255) not null,
  status varchar(50) not null,
  last_device_id bigint constraint fk_notification_push_recipeint_device_user references device_user,
  subscription_id bigint not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null,
  constraint uk_push_notification_recipient_type_tkn_subs_id_app_type unique (type, destination_route_token, subscription_id, application_type)
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
  id bigint not null constraint notification_push_recipient_sns_pkey
    primary key constraint fk_notification_push_recipient_sns_notification_push_recipient references notification_push_recipient,
  platform_application_arn varchar(500) not null
);

create table if not exists notification_push_subscription (
  id bigint not null constraint pk_notification_push_subscription primary key,
  nms_user_id bigint not null constraint uk_notification_push_recipeint_sns_ unique constraint fk_notification_push_subscription_nms_user references nms_user,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);

alter table notification_push_recipient add constraint fk_notification_push_recipient_subscription foreign key (subscription_id) references notification_push_subscription;

create table if not exists notification_push_subscription_request (
  id bigint not null constraint pk_notification_push_subscription_request primary key,
  uuid varchar(36) not null constraint uk_notification_push_subscription_request unique,
  application_type varchar(50) not null,
  previous_subscription_request_uuid varchar(36),
  state varchar(50) not null,
  subscribe boolean not null,
  recipient_id bigint constraint fk_notification_push_subscription_request_recipient references notification_push_recipient,
  nms_user_id bigint not null constraint fk_notification_push_subscription_request_nms_user references nms_user,
  user_device_id bigint not null constraint fk_notification_push_subscription_request_device_user references device_user,
  user_device_token varchar(2000) not null,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);

create table if not exists notification_sms (
  id bigint not null constraint pk_notification_sms primary key constraint fk_notification_sms_notification references notification,
  recipient_mobile_number varchar(255) not null
);

create table if not exists notification_user (
  id bigint not null constraint pk_notification_user primary key,
  notification_id bigint not null constraint uk_notification_usernotification_sms_notification_id unique
    constraint fk_notification_usernotification_sms_notification references notification,
  nms_user_id bigint not null constraint fk_notification_usernotification_sms_nms_user references nms_user,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);