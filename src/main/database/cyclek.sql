-- Create the cyclek database
CREATE DATABASE cyclek;

-- Connect to the cyclek database
\c cyclek;

--create tables
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

-- create the FOODS table
CREATE TABLE foods
(
    id_food     SERIAL PRIMARY KEY,
    fdnm        VARCHAR(50) NOT NULL,
    kcal        INTEGER NOT NULL,
    fats        INTEGER NOT NULL,
    carbohydrates        INTEGER NOT NULL,
    proteins    INTEGER NOT NULL
);

--create the MEAL table
CREATE TABLE meal (
  id            SERIAL PRIMARY KEY,
  ID_UTE        INT NOT NULL,
  ID_PASTO      INT NOT NULL,
  GIORNO        DATE NOT NULL,
  PASTO         VARCHAR(50) NOT NULL
);

--insert basic data in the tables
-- Insert some sample data for testing
INSERT INTO login (id, username, password)
VALUES (1, 'user1', 'password1'),
       (2, 'user2', 'password2'),
       (3, 'user3', 'password3');

INSERT INTO users (name, surname, date, sex)
VALUES ('Paolo', 'Rossi', '1995-10-08', 'M'),
       ('Paolo', 'Bianchi', '1995-10-08', 'M'),
       ('Lucia', 'Rossi', '1995-10-08', 'F');

INSERT INTO foods (fdnm, kcal, fats, carbohydrates, proteins)
VALUES ('Pollo', 239, 14, 0, 27),
       ('Riso', 130, 0, 28, 3),
       ('Broccoli', 34, 0, 7, 3);