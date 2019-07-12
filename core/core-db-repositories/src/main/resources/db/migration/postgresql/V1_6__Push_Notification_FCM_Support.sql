alter table notification_push_recipient add column	platform_application_arn varchar(255);
update notification_push_recipient rc set platform_application_arn = rc_sns.platform_application_arn
    from notification_push_recipient_sns rc_sns where rc_sns.id=rc.id;
alter table notification alter column provider_external_uuid type varchar(320);