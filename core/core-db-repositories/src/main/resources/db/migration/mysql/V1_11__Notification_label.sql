CREATE TABLE IF NOT EXISTS notification_label (
  id BIGINT not null AUTO_INCREMENT PRIMARY KEY,
  label_name VARCHAR(255) NOT NULL,
  notification_id BIGINT,
  CONSTRAINT fk_notification_label_notification FOREIGN KEY (notification_id) REFERENCES notification (id),
  created DATETIME NOT NULL,
  removed DATETIME,
  updated DATETIME NOT NULL
);