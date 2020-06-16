alter table notification_push
    add column template_name varchar(255) default null;

alter table notification_push
    add column locale varchar(5) default null;

alter table notification_email
    alter column locale type varchar(5) using locale::varchar(5);

alter table notification_sms
    alter column locale type varchar(5) using locale::varchar(5);