
alter table notification add column if not exists has_secure_properties bit(1) not null default false;

update notification
set has_secure_properties = false
where has_secure_properties is null;