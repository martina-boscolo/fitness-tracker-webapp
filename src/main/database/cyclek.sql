-- Create the cyclek database

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


-- Create the exercise_category table
CREATE TABLE exercise_category (
    category_id INT PRIMARY KEY,
    category_name VARCHAR(100) NOT NULL
);

-- Create the exercise table with a foreign key constraint referencing the exercise_category table
CREATE TABLE exercise (
     exercise_id INT PRIMARY KEY,
     exercise_name VARCHAR(100) NOT NULL,
     description TEXT,
     exercise_equipment VARCHAR(100),
      category_id INT,
        FOREIGN KEY (category_id) REFERENCES exercise_category(category_id)
);


Drop table posts, comments, likes_dislikes;
--create the posts table
CREATE TABLE posts
(
    post_id       SERIAL PRIMARY KEY,
    user_id       INTEGER NOT NULL REFERENCES users (id),
    text_content  TEXT NOT NULL,
    image_path    TEXT, -- images should be stored in the filesystem
    like_count    INTEGER   DEFAULT 0,
    dislike_count INTEGER   DEFAULT 0,
    comment_count INTEGER   DEFAULT 0,
    post_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



--create the likes_dislikes table
CREATE TABLE likes_dislikes
(
    like_dislike_id SERIAL PRIMARY KEY,
    user_id         INTEGER NOT NULL REFERENCES users (id),
    post_id         INTEGER NOT NULL REFERENCES posts (post_id),
    is_like         BOOLEAN NOT NULL,
    CONSTRAINT unique_post_user_combination UNIQUE (post_id, user_id) -- only like or dislike on the same post by the same user
);

--create the comments table
CREATE TABLE comments
(
    comment_id   SERIAL PRIMARY KEY,
    user_id      INTEGER NOT NULL REFERENCES users (id),
    post_id      INTEGER NOT NULL REFERENCES posts (post_id),
    text_content TEXT NOT NULL
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

INSERT INTO posts (user_id, text_content, image_path, like_count, dislike_count, comment_count, post_date)
VALUES
    (1, 'Just finished a 5-mile run! Feeling great!', '/fitness/images/run.jpg', 10, 2, 5, '2024-04-07 08:30:00'),
    (2, 'Leg day at the gym was intense!', '/fitness/images/legday.jpg', 15, 1, 8, '2024-04-06 17:45:00'),
    (3, 'Healthy breakfast: oatmeal with fruits and nuts 🥣', NULL, 20, 0, 12, '2024-04-05 09:00:00'),
    (1, 'Completed my first marathon! What an achievement!', '/fitness/images/marathon.jpg', 50, 3, 25, '2024-04-04 11:20:00'),
    (2, 'Back to the gym after a long break 💪', '/fitness/images/gym.jpg', 30, 5, 15, '2024-04-03 18:00:00');


INSERT INTO likes_dislikes (user_id, post_id, is_like)
VALUES
    (1, 1, TRUE),
    (2, 1, TRUE),
    (3, 1, TRUE),
    (1, 2, TRUE),
    (3, 3, TRUE),
    (1, 3, FALSE),
    (2, 3, FALSE);

INSERT INTO comments (user_id, post_id, text_content)
VALUES
    (1, 1, 'Great job!'),
    (2, 1, 'Keep it up!'),
    (3, 2, 'Love running in the morning!'),
    (1, 3, 'Leg day is always tough!'),
    (3, 3, 'Nice progress!');


-- Insert sample data into the exercise_category table
INSERT INTO exercise_category (category_id, category_name)
VALUES
    (1, 'Strength'),
    (2, 'Cardio'),
    (3, 'Flexibility'),
    (4, 'Balance');
-- insert data to exercise table
INSERT INTO exercise(exercise_id, exercise_name, description, exercise_equipment, category_id)
    VALUES
        (1,'squat','this is a description1','Barbell',1)