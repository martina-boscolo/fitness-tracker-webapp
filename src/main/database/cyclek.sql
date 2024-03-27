-- Create the cyclek database
CREATE DATABASE cyclek;

-- Connect to the cyclek database
\c cyclek;

-- Create the login table
CREATE TABLE login (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL
);

-- Insert some sample data for testing
INSERT INTO login (username, password) VALUES 
    ('user1', 'password1'),
    ('user2', 'password2'),
    ('user3', 'password3');