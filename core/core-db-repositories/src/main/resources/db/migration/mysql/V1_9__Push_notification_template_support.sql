alter table notification_push
    add column template_name varchar(255) default null;

alter table notification_push
    add column locale char(2) default null;
