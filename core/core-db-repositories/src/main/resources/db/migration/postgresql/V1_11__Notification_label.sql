create table if not exists notification_label (
  id bigint not null constraint pk_notification_label primary key,
  label_name varchar(255) not null,
  notification_id bigint constraint fk_notification_label_notification references notification,
  created timestamp not null,
  removed timestamp,
  updated timestamp not null
);