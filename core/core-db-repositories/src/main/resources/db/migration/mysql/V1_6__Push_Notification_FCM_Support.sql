alter table notification_push_recipient add column platform_application_arn varchar(255);
update notification_push_recipient rc
    inner join notification_push_recipient_sns rc_sns on rc_sns.id = rc.id
set rc.platform_application_arn = rc_sns.platform_application_arn;
alter table notification change column provider_external_uuid provider_external_uuid varchar(320);
