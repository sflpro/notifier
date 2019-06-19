
alter table notification add column has_secure_properties boolean not null default false;

update notification set has_secure_properties = false;