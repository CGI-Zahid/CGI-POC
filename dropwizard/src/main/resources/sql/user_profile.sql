--liquibase formatted SQL

--changeset valonsejdini:1
-- COMMENT :Creating user table for storing registered residents and admins.
SET foreign_key_checks = 0;
DROP TABLE IF EXISTS user;
SET foreign_key_checks = 1;

CREATE TABLE user (
  id         BIGINT                     NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(65)                NOT NULL,
  last_name  VARCHAR(65)                NOT NULL,
  email      VARCHAR(150)               NOT NULL,
  password   VARCHAR(150)               NOT NULL,
  phone      VARCHAR(20)                NOT NULL,
  zip_code   VARCHAR(13)                NOT NULL,
  role       ENUM ('ADMIN', 'RESIDENT') NOT NULL,
  latitude   DECIMAL(10, 8)             NOT NULL,
  longitude  DECIMAL(11, 8)             NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY email (email)
)
  AUTO_INCREMENT = 1;

-- COMMENT :Creating user_notification TABLE.
CREATE TABLE user_notification (
  user_id         BIGINT NOT NULL,
  notification_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, notification_id),
  FOREIGN KEY (user_id) REFERENCES user (id)
);

-- ROLLBACK DROP DROP TABLE user; DROP DROP TABLE notification_method; DROP DROP TABLE user_notification;
