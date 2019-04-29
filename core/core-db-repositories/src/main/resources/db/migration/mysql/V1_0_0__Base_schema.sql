CREATE TABLE IF NOT EXISTS device_user (
  id BIGINT NOT NULL primary key,
  uuid VARCHAR(36) NOT NULL,
  os_type VARCHAR(50) NOT NULL,
  nms_user_id BIGINT NOT NULL,
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL,
  CONSTRAINT uk_device_user_nms_user_id_uuid UNIQUE (nms_user_id, uuid)
);

CREATE TABLE IF NOT EXISTS nms_user (
  id BIGINT NOT NULL PRIMARY KEY,
  uuid VARCHAR(36) NOT NULL UNIQUE,
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL
);

ALTER TABLE device_user ADD CONSTRAINT fk_device_user_nms_user FOREIGN KEY (nms_user_id) REFERENCES nms_user (id);

CREATE TABLE IF NOT EXISTS notification (
  id BIGINT NOT NULL PRIMARY KEY,
  uuid VARCHAR(36) NOT NULL UNIQUE,
  type VARCHAR(31) NOT NULL,
  client_ip_address VARCHAR(255),
  content VARCHAR(20000),
  provider_external_uuid VARCHAR(36),
  provider_type VARCHAR(50) NOT NULL,
  state VARCHAR(50) NOT NULL,
  subject VARCHAR(255),
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS notification_email (
  id BIGINT NOT NULL PRIMARY KEY,
  recipient_email varchar(255) not null,
  sender_email varchar(255),
  template_name varchar(255),
  CONSTRAINT fk_notification_email_notification FOREIGN KEY (id) REFERENCES notification (id)
);

CREATE TABLE IF NOT EXISTS notification_email_third_party (
  id BIGINT NOT NULL PRIMARY KEY,
  CONSTRAINT fk_notification_email_third_party_notification_email FOREIGN KEY (id) references notification_email (id)
);

CREATE TABLE IF NOT EXISTS notification_email_third_party_property (
  id BIGINT NOT NULL PRIMARY KEY,
  notification_id BIGINT NOT NULL,
  property_key varchar(255) NOT NULL,
  property_value varchar(4000),
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL,
  constraint fk_notif_third_party_property_notif_third_party  FOREIGN KEY (id) references notification_email_third_party (id)
);

CREATE TABLE IF NOT EXISTS notification_push (
  id BIGINT NOT NULL PRIMARY KEY,
  recipient_id bigint NOT NULL,
  CONSTRAINT fk_notification_push_notification FOREIGN KEY (id) references notification (id)
);

CREATE TABLE IF NOT EXISTS notification_push_property (
  id BIGINT NOT NULL PRIMARY KEY,
  push_notification_id BIGINT NOT NULL,
  property_key VARCHAR(255) NOT NULL,
  property_value VARCHAR(255) NOT NULL,
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL,
  CONSTRAINT fk_notification_push_property_notification_push FOREIGN KEY (push_notification_id) REFERENCES notification_push (id)
);

CREATE TABLE IF NOT EXISTS notification_push_recipient (
  id BIGINT NOT NULL PRIMARY KEY,
  uuid VARCHAR(36) NOT NULL UNIQUE,
  type VARCHAR(31) NOT NULL,
  application_type VARCHAR(50) NOT NULL,
  destination_route_token VARCHAR(255) NOT NULL,
  device_operating_system_type VARCHAR(255) NOT NULL,
  status VARCHAR(50) NOT NULL,
  last_device_id BIGINT,
  subscription_id BIGINT NOT NULL,
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL,
  CONSTRAINT uk_push_notification_recipient_type_tkn_subs_id_app_type UNIQUE (type, destination_route_token, subscription_id, application_type),
  CONSTRAINT fk_notification_push_recipeint_device_user FOREIGN KEY (last_device_id) REFERENCES device_user (id),
  INDEX idx_push_notification_recipient_type (type),
  INDEX idx_push_notification_application_type (application_type),
  INDEX idx_push_notification_recipient_destination_route_token (destination_route_token),
  INDEX idx_push_notification_recipient_device_operating_system_type (device_operating_system_type),
  INDEX idx_push_notification_recipient_status (status)
);

ALTER TABLE notification_push
  ADD CONSTRAINT fk_notification_push_notification_push_recipient FOREIGN KEY (recipient_id) REFERENCES notification_push_recipient (id);

CREATE TABLE IF NOT EXISTS notification_push_recipient_device (
  id BIGINT NOT NULL primary key,
  device_id BIGINT NOT NULL ,
  recipient_id BIGINT NOT NULL,
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL,
  CONSTRAINT uk_push_notification_recipient_device_device_recipient UNIQUE (device_id, recipient_id),
  CONSTRAINT fk_notification_push_recipient_device_device_user FOREIGN KEY (device_id) REFERENCES device_user (id),
  CONSTRAINT fk_notif_push_recipient_device_notification_push_reciepeint FOREIGN KEY (recipient_id) REFERENCES notification_push_recipient (id)
);

CREATE TABLE IF NOT EXISTS notification_push_recipient_sns (
  id BIGINT NOT NULL PRIMARY KEY,
  platform_application_arn VARCHAR(500) NOT NULL,
  CONSTRAINT fk_notification_push_recipient_sns_notification_push_recipient FOREIGN KEY (id) REFERENCES notification_push_recipient (id)
);

CREATE TABLE IF NOT EXISTS notification_push_subscription (
  id BIGINT NOT NULL PRIMARY KEY,
  nms_user_id BIGINT NOT NULL UNIQUE ,
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL,
  CONSTRAINT fk_notification_push_subscription_nms_user FOREIGN KEY (nms_user_id) REFERENCES nms_user (id)
);

ALTER TABLE notification_push_recipient
  ADD CONSTRAINT fk_notification_push_recipient_subscription FOREIGN KEY (subscription_id) REFERENCES notification_push_subscription (id);

CREATE TABLE IF NOT EXISTS notification_push_subscription_request (
  id BIGINT NOT NULL PRIMARY KEY ,
  uuid VARCHAR(36) NOT NULL UNIQUE,
  application_type VARCHAR(50) NOT NULL,
  previous_subscription_request_uuid VARCHAR(36),
  state VARCHAR(50) NOT NULL,
  subscribe BIT(1) NOT NULL,
  recipient_id BIGINT,
  nms_user_id BIGINT NOT NULL,
  user_device_id BIGINT NOT NULL ,
  user_device_token VARCHAR(2000) NOT NULL,
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL,
  CONSTRAINT fk_notification_push_subscription_request_recipient FOREIGN KEY (recipient_id) references notification_push_recipient (id),
  CONSTRAINT fk_notification_push_subscription_request_nms_user FOREIGN KEY (nms_user_id) references nms_user (id),
  CONSTRAINT fk_notification_push_subscription_request_device_user FOREIGN KEY (user_device_id) references device_user (id)
);

CREATE TABLE IF NOT EXISTS notification_sms (
  id BIGINT NOT NULL PRIMARY KEY ,
  recipient_mobile_number VARCHAR(255) NOT NULL,
  CONSTRAINT fk_notification_sms_notification FOREIGN KEY (id) REFERENCES notification (id)
);

CREATE TABLE IF NOT EXISTS notification_usernotification_sms (
  id BIGINT NOT NULL PRIMARY KEY,
  notification_id BIGINT NOT NULL UNIQUE,
  nms_user_id bigint NOT NULL ,
  created DATETIME NOT NULL ,
  removed DATETIME,
  updated DATETIME NOT NULL,
  CONSTRAINT fk_notification_usernotification_sms_notification FOREIGN KEY (notification_id) references notification (id),
  CONSTRAINT fk_notification_usernotification_sms_nms_user FOREIGN KEY (nms_user_id) references nms_user (id)
);