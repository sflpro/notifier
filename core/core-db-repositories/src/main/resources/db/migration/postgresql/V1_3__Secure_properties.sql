
alter table notification add column if not exists has_secure_properties boolean not null default false;

update notification
set has_secure_properties = false
where has_secure_properties is null;