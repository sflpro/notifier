CREATE TABLE IF NOT EXISTS notification_email_reply_to (
  id BIGINT not null AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  notification_email_id BIGINT NOT NULL,
  CONSTRAINT fk_notification_email_reply_to_notification_email FOREIGN KEY (notification_email_id) REFERENCES notification_email (id),
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL
);