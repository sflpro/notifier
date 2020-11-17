create table if not exists notification_email_reply_to (
  id bigint not null constraint pk_notification_email_reply_to primary key,
  email varchar(255) not null,
  notification_email_id bigint constraint fk_notification_email_reply_to_notification_email references notification_email,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);
