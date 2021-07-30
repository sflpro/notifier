alter table notification
    add column sending_priority varchar(50) null;

update notification
set sending_priority = 'NORMAL'
where sending_priority is null;

alter table notification
    alter column sending_priority set not null;