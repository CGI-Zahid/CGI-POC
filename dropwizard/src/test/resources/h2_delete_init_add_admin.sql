SET foreign_key_checks = 0;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS notification_method;
DROP TABLE IF EXISTS user_notification;
SET foreign_key_checks = 1;

CREATE TABLE user (
  id         BIGINT         NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(65)    NOT NULL,
  last_name  VARCHAR(65)    NOT NULL,
  email      VARCHAR(150)   NOT NULL,
  password   VARCHAR(150)   NOT NULL,
  phone      VARCHAR(20)    NOT NULL,
  zip_code   VARCHAR(13)    NOT NULL,
  role       VARCHAR(8)     NOT NULL,
  latitude   DECIMAL(10, 8) NOT NULL,
  longitude  DECIMAL(11, 8) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY email (email)
)
  AUTO_INCREMENT = 1;

/* insert admin user with password adminpw */
INSERT INTO user (id, first_name, last_name, email, password, phone, zip_code, role, latitude, longitude, geo_loc_latitude, geo_loc_longitude)
VALUES (1,
        'john',
        'smith',
        'admin@cgi.com',
        '518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af',
        '123-456-7890',
        '95814',
        'ADMIN',
        38.5824933,
        -121.4941738,
        0.0,
        0.0
);


CREATE TABLE user_notification (
  user_id         BIGINT NOT NULL,
  notification_id BIGINT NOT NULL,
  PRIMARY KEY (user_id, notification_id),
  FOREIGN KEY (user_id) REFERENCES user (id),
);

