
alter table notification add column has_secure_properties bit(1) not null default false;

update notification
set has_secure_properties = false
where has_secure_properties is null;