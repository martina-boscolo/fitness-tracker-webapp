-- Connect to the cyclek database
\c cyclek;

--create tables

-- create the users table
CREATE TABLE users (
                       id          SERIAL PRIMARY KEY,
                       name        VARCHAR(50) NOT NULL,
                       surname     VARCHAR(50) NOT NULL,
                       birthday    DATE        NOT NULL,
                       gender      VARCHAR(1)
);

-- Create the login table
CREATE TABLE login (
                       id          SERIAL PRIMARY KEY,
                       id_user     INTEGER NOT NULL REFERENCES users (id),
                       username    VARCHAR(50) NOT NULL,
                       password    VARCHAR(50) NOT NULL
);

-- create the FOODS table
CREATE TABLE foods (
                       id              SERIAL PRIMARY KEY,
                       fdnm            VARCHAR(50) NOT NULL,
                       kcal            INTEGER NOT NULL,
                       fats            INTEGER NOT NULL,
                       carbohydrates   INTEGER NOT NULL,
                       proteins        INTEGER NOT NULL
);

--create the MEAL table
CREATE TABLE meal (
                      id           SERIAL PRIMARY KEY,
                      id_user      INTEGER NOT NULL REFERENCES users (id),
                      id_meal      INT NOT NULL,
                      day          DATE NOT NULL,
                      meal         VARCHAR(50) NOT NULL
);

-- Create Diet Plans Table
CREATE TABLE dietplans (
                           id          SERIAL PRIMARY KEY,
                           id_user     INTEGER NOT NULL REFERENCES users (id),
                           plan_name   VARCHAR(50) NOT NULL,
                           diet        JSON NOT NULL
);

--create table for body statistics
CREATE TABLE body_stats
(
    id          SERIAL PRIMARY KEY,
    id_user     INTEGER NOT NULL REFERENCES users (id),
    weight      FLOAT NOT NULL,
    height      FLOAT NOT NULL,
    fatty       FLOAT NOT NULL,
    lean        FLOAT NOT NULL,
    stats_date  DATE NOT NULL
);


-- Create the exercise_category table
CREATE TABLE exercise_category (
                                   id              SERIAL PRIMARY KEY,
                                   category_name   VARCHAR(100) NOT NULL
);

-- Create the exercise table with a foreign key constraint referencing the exercise_category table
CREATE TABLE exercise (
                          id                 SERIAL PRIMARY KEY,
                          id_category        INTEGER NOT NULL REFERENCES exercise_category(id),
                          exercise_name      VARCHAR(100) NOT NULL,
                          description        TEXT,
                          exercise_equipment VARCHAR(100)
);


--create the posts table
CREATE TABLE posts
(
    id              SERIAL PRIMARY KEY,
    id_user         INTEGER NOT NULL REFERENCES users (id),
    text_content    TEXT NOT NULL,
    photo           BYTEA,
    photoMediaType  TEXT,
    post_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--create the likes table
CREATE TABLE likes
(
    id          SERIAL PRIMARY KEY,
    id_user     INTEGER NOT NULL REFERENCES users (id),
    id_post     INTEGER NOT NULL REFERENCES posts (id),

    CONSTRAINT unique_post_user_combination UNIQUE (id_post, id_user) -- only one like on the same post by the same user
);

--create the comments table
CREATE TABLE comments
(
    id           SERIAL PRIMARY KEY,
    id_user      INTEGER NOT NULL REFERENCES users (id),
    id_post      INTEGER NOT NULL REFERENCES posts (id),
    text_content TEXT NOT NULL
);


--insert basic data in the tables
-- Insert some sample data for testing
INSERT INTO users (name, surname, birthday, gender)
VALUES ('Paolo', 'Rossi', '1995-10-08', 'M'),
       ('Paolo', 'Bianchi', '1995-10-08', 'M'),
       ('Lucia', 'Rossi', '1995-10-08', 'F');

INSERT INTO login (id_user, username, password)
VALUES (1, 'user1', 'password1'),
       (2, 'user2', 'password2'),
       (3, 'user3', 'password3');

INSERT INTO foods (fdnm, kcal, fats, carbohydrates, proteins)
VALUES ('Pollo', 239, 14, 0, 27),
       ('Riso', 130, 0, 28, 3),
       ('Broccoli', 34, 0, 7, 3);

INSERT INTO dietplans (id_user, plan_name, diet)
VALUES
    (2, 'Eman', '{"Monday":{"Breakfast":{"Scrambledeggs":150,"Spinach":50,"Wholegraintoast":50},"Lunch":{"Quinoa":100,"Mixedvegetables":{"bellpeppers":50,"cucumber":50,"tomato":50},"Grilledchicken":150},"Dinner":{"Salmon":150,"Broccoli":100,"Brownrice":100}},"Tuesday":{"Breakfast":{"Greekyogurt":150,"Strawberries":100,"Granola":30},"Lunch":{"Turkey":100,"Avocado":50,"Lettuce":50,"Tomato":50,"Carrotsticks":100},"Dinner":{"Kidneybeans":100,"Corn":100,"Bellpeppers":50,"Wholegraingarlicbread":50}},"Wednesday":{"Breakfast":{"Oatmeal":150,"Banana":100,"Honey":15},"Lunch":{"Grilledvegetables":{"zucchini":100,"eggplant":100,"redonion":50,"roastedredpepper":50},"Salad":150},"Dinner":{"Leanbeef":150,"Broccoli":100,"Bellpeppers":50,"Snappeas":50,"Brownrice":100}},"Thursday":{"Breakfast":{"Spinach":50,"Banana":100,"Almondmilk":150,"Proteinpowder":30},"Lunch":{"Quinoa":100,"Bellpeppers":50,"Blackbeans":100,"Corn":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chickenbreast":150,"Sweetpotatoes":150,"Greenbeans":100}},"Friday":{"Breakfast":{"Wholegraintoast":50,"Avocado":50,"Boiledeggs":100},"Lunch":{"Tuna":100,"Mixedgreens":100,"Cherrytomatoes":50,"Balsamicvinaigrettedressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snowpeas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Wholewheatpancakes":100,"Berries":100,"Maplesyrup":30},"Lunch":{"Grilledshrimp":150,"Mixedgreens":100,"Cucumber":50,"Citrusvinaigrettedressing":30},"Dinner":{"Spaghettisquash":200,"Turkeymeatballs":150,"Asparagus":100}},"Sunday":{"Breakfast":{"Veggieomelette":150,"Mushrooms":50,"Onions":50,"Bellpeppers":50,"Cheese":50},"Lunch":{"Lentilsoup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Wholegraincrackers":50},"Dinner":{"Tilapia":150,"Brusselssprouts":100,"Quinoapilaf":100}}}');

INSERT INTO body_stats (id_user, weight, height, fatty, lean, stats_date)
VALUES
    (1, 82, 175, 14.8, 20, '2024-01-02 08:30:00'),
    (2, 65.8, 190, 14, 30.6, '2024-04-03 16:12:00'),
    (3, 82, 165, 27.2, 28.6, '2024-02-07 12:30:00'),
    (3, 80, 165, 2.2, 29, '2024-02-11 12:30:00'),
    (1, 78, 175, 13.6, 23.5, '2024-04-09 09:43:00');

INSERT INTO posts (id_user, text_content, photo, photoMediaType, post_date)
VALUES
    (1, 'Just finished a 5-mile run! Feeling great!', NULL, 'image/png',  '2024-04-07 08:30:00'),
    (2, 'Leg day at the gym was intense!', NULL, 'image/png' ,'2024-04-06 17:45:00'),
    (3, 'Healthy breakfast: oatmeal with fruits and nuts 🥣', NULL, 'image/png', '2024-04-05 09:00:00'),
    (1, 'Completed my first marathon! What an achievement!', NULL, 'image/png',   '2024-04-04 11:20:00'),
    (2, 'Back to the gym after a long break 💪',NULL, NULL,  '2024-04-03 18:00:00');


INSERT INTO likes (id_user, id_post)
VALUES
    (1, 1),
    (2, 1),
    (3, 1),
    (1, 2),
    (3, 3),
    (1, 3),
    (2, 3);

INSERT INTO comments (id_user, id_post, text_content)
VALUES
    (1, 1, 'Great job!'),
    (2, 1, 'Keep it up!'),
    (3, 2, 'Love running in the morning!'),
    (1, 3, 'Leg day is always tough!'),
    (3, 3, 'Nice progress!');


-- Insert sample data into the exercise_category table
INSERT INTO exercise_category (category_name)
VALUES
    ('Strength'),
    ('Cardio'),
    ('Flexibility'),
    ('Balance');
-- insert data to exercise table
INSERT INTO exercise(exercise_name, description, exercise_equipment, id_category)
VALUES
    ('squat','this is a description1','Barbell',1)

