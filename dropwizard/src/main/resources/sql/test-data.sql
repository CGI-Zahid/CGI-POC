--liquibase formatted SQL

--changeset valonsejdini:1
-- COMMENT : Loading tables with helper admin user.

/* insert ADMIN user with password adminpw */
INSERT INTO user (id, first_name, last_name, email, password, phone, address, address_additional_info, city, state, zip_code, role, latitude, longitude, geo_loc_latitude, geo_loc_longitude)
VALUES ( 1,
'john',
'smith',
'admin@cgi.com',
'518bd5283161f69a6278981ad00f4b09a2603085f145426ba8800c:8bd85a69ed2cb94f4b9694d67e3009909467769c56094fc0fce5af',
'1234567890',
'required street',
'optional street',
'Sacramento',
'California',
'95814',
'ADMIN',
38.5824933,
-121.4941738,
0.0,
0.0
);
