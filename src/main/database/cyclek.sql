-- Create the cyclek database
CREATE DATABASE cyclek;

-- Connect to the cyclek database
\c cyclek;

-- Create the login table
CREATE TABLE login
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

-- create the users table
CREATE TABLE users
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(50) NOT NULL,
    surname VARCHAR(50) NOT NULL,
    date    DATE        NOT NULL,
    sex     char
);


-- Insert some sample data for testing
INSERT INTO login (id, username, password)
VALUES (1, 'user1', 'password1'),
       (2, 'user2', 'password2'),
       (3, 'user3', 'password3');

INSERT INTO users (id, name, surname, date, sex)
VALUES (1, 'Paolo', 'Rossi', 08 / 10 / 1995, 'M'),
       (2, 'Paolo', 'Bianchi', 05 / 8 / 1978, 'M'),
       (3, 'Lucia', 'Rossi', 08 / 10 / 1995, 'F');