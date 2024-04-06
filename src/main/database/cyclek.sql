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
    sex     VARCHAR(1)  NOT NULL
);

-- Insert some sample data for testing
INSERT INTO login (username, password)
VALUES ('user1', 'password1'),
       ('user2', 'password2'),
       ('user3', 'password3');

INSERT INTO users (name, surname, date, sex)
VALUES ('Paolo', 'Rossi', '1995-10-08', 'M'),
       ('Paolo', 'Bianchi', '1978-08-05', 'M'),
       ('Lucia', 'Rossi', '1995-10-08', 'F');

