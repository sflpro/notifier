CREATE TABLE IF NOT EXISTS notification_email_file_attachment (
  id BIGINT not null AUTO_INCREMENT PRIMARY KEY,
  file_name VARCHAR(255) NOT NULL,
  url VARCHAR(1000) NOT NULL,
  mime_type VARCHAR(255),
  notification_email_id BIGINT NOT NULL,
  CONSTRAINT fk_notification_email_file_attachment_notification_email FOREIGN KEY (notification_email_id) REFERENCES notification_email (id),
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL
);