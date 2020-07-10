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