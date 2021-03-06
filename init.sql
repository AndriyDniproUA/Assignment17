CREATE DATABASE contacts;

CREATE TABLE users(
user_id SERIAL PRIMARY KEY,
user_login VARCHAR(128) NOT NULL UNIQUE,
user_password VARCHAR(128) NOT NULL,
user_dateofbirth DATE);

CREATE TABLE contactlist(
contact_id SERIAL PRIMARY KEY,
contact_owner INT NOT NULL,
contact_name VARCHAR(128) NOT NULL,
contact_type VARCHAR(10) NOT NULL,
contact_value VARCHAR(128) NOT NULL,
CONSTRAINT contact_owner_user_id_fk
FOREIGN KEY (contact_owner) REFERENCES users (user_id) ON DELETE RESTRICT);




