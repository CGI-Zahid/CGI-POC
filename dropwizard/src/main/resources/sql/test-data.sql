--liquibase formatted SQL

--changeset valonsejdini:1
-- COMMENT : Loading tables with helper admin user.

/* insert ADMIN user with password adminpw */
INSERT INTO user (id, first_name, last_name, email, password, phone, zip_code, role, latitude, longitude)
VALUES ( 1,
'john',
'smith',
'admin@cgi.com',
'518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af',
'123-456-7890',
'95814',
'ADMIN',
38.5824933,
-121.4941738
);