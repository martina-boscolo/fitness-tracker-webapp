-- connection
\c cyclek;

-- CREATION

-- Marco
CREATE TABLE users (
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50) NOT NULL,
    surname     VARCHAR(50) NOT NULL,
    birthday    DATE        NOT NULL,
    gender      VARCHAR(1),
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(50) NOT NULL
);

-- Alessio
CREATE TABLE userStats
(
    id          SERIAL PRIMARY KEY,
    idUser      INTEGER NOT NULL REFERENCES users (id),
    weight      FLOAT NOT NULL,
    height      FLOAT NOT NULL,
    fatty       FLOAT NOT NULL,
    lean        FLOAT NOT NULL,
    statsDate   DATE DEFAULT CURRENT_DATE,

    CONSTRAINT unique_daily_stats UNIQUE (idUser, statsDate)
);

CREATE TABLE userGoals
(
    id          SERIAL PRIMARY KEY,
    idUser      INTEGER NOT NULL REFERENCES users (id),
    weight      FLOAT NOT NULL,
    height      FLOAT NOT NULL,
    fatty       FLOAT NOT NULL,
    lean        FLOAT NOT NULL,
    goalDate     DATE DEFAULT CURRENT_DATE,

    CONSTRAINT unique_daily_obj UNIQUE (idUser, goalDate)
);

-- Martina
CREATE TABLE posts
(
    id              SERIAL PRIMARY KEY,
    id_user         INTEGER NOT NULL REFERENCES users (id),
    text_content    TEXT NOT NULL,
    photo           BYTEA,
    photoMediaType  TEXT,
    post_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    likes_count     INTEGER DEFAULT 0,
    comments_count  INTEGER DEFAULT 0
);

CREATE TABLE likes
(
    id          SERIAL PRIMARY KEY,
    id_user     INTEGER NOT NULL REFERENCES users (id),
    id_post     INTEGER NOT NULL REFERENCES posts (id) ON DELETE CASCADE,

    CONSTRAINT unique_post_user_combination UNIQUE (id_post, id_user) -- only one like on the same post by the same user
);

CREATE TABLE comments
(
    id           SERIAL PRIMARY KEY,
    id_user      INTEGER NOT NULL REFERENCES users (id),
    id_post      INTEGER NOT NULL REFERENCES posts (id) ON DELETE CASCADE,
    text_content TEXT NOT NULL
);

-- Giacomo
CREATE TABLE dietplans (
    id         SERIAL PRIMARY KEY,
    idUser     INTEGER NOT NULL REFERENCES users (id),
    planName   VARCHAR(50) NOT NULL,
    diet       JSON NOT NULL,
    dietDate   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Riccardo
CREATE TABLE foods (
    id              SERIAL PRIMARY KEY,
    fdnm            VARCHAR(50) NOT NULL,
    kcal            INTEGER NOT NULL,
    fats            INTEGER NOT NULL,
    carbohydrates   INTEGER NOT NULL,
    proteins        INTEGER NOT NULL
);

CREATE TABLE meal (
    id              SERIAL PRIMARY KEY,
    id_user         INTEGER NOT NULL REFERENCES users (id),
    meal_date       DATE NOT NULL DEFAULT CURRENT_DATE,
    meal_type       INTEGER NOT NULL,
    meal            JSON NOT NULL,
    
    CONSTRAINT unique_user_date_type_combination UNIQUE (id_user, meal_date, meal_type)
);

-- Kimia
CREATE TABLE exercise_category (
    id              SERIAL PRIMARY KEY,
    category_name   VARCHAR(100) NOT NULL
);

CREATE TABLE exercises (
    id                 SERIAL PRIMARY KEY,
    id_category        INTEGER NOT NULL REFERENCES exercise_category(id),
    exercise_name      VARCHAR(100) NOT NULL,
    description        TEXT,
    exercise_equipment VARCHAR(100)
);

CREATE TABLE exercise_plan (
    id          SERIAL PRIMARY KEY,
    idUser      INTEGER NOT NULL REFERENCES users (id),
    planName    VARCHAR(50) NOT NULL,
    plan        JSON NOT NULL,
    planDate    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- INSERT

-- Marco
INSERT INTO users (name, surname, birthday, gender, username, password)
VALUES 
    ('Paolo', 'Rossi', '1995-10-08', 'M', 'user1', '123456'),
    ('Paolo', 'Bianchi', '1995-10-08', 'M', 'user2', '123456'),
    ('Lucia', 'Rossi', '1995-10-08', 'F', 'user3', '123456');

-- Alessio
INSERT INTO userStats (idUser, weight, height, fatty, lean, statsDate)
VALUES
    (1, 70, 175, 11.1, 30.2, '2024-01-01 08:10:00'),
    (1, 70, 175, 11.3, 31.3, '2024-01-05 09:20:01'),
    (1, 72, 175, 11.6, 31.6, '2024-02-10 10:30:12'),
    (1, 73, 175, 12.0, 32.2, '2024-02-15 11:12:02'),
    (1, 76, 175, 11.8, 33.7, '2024-03-20 12:24:23'),
    (1, 77, 175, 11.7, 34.1, '2024-04-25 13:36:03'),
    (1, 80, 175, 12.0, 35.0, '2024-04-30 14:22:34'),
    (1, 82, 175, 12.0, 35.9, '2024-05-05 15:33:04'),
    (1, 83, 175, 12.1, 36.2, '2024-05-10 16:44:45'),
    (2, 65.8, 190, 14, 30.6, '2024-04-03 16:12:00'),
    (3, 82, 165, 27.2, 28.6, '2024-02-07 12:30:00'),
    (3, 80, 165, 2.2, 29,    '2024-02-11 12:30:00');

INSERT INTO userGoals (idUser, weight, height, fatty, lean, goalDate)
VALUES
    (1, 75, 175, 11.5, 33.0, '2024-03-01 08:10:00'),
    (1, 80, 175, 12.0, 36.0, '2024-05-01 09:20:01'),
    (1, 85, 175, 12.0, 37.0, '2024-07-01 10:30:12'),
    (2, 65.8, 190, 14, 30.6, '2024-04-03 16:12:00'),
    (3, 80, 165, 2.2, 29, '2024-02-11 12:30:00');

-- Martina
INSERT INTO posts (id_user, text_content, photo, photoMediaType, post_date, likes_count, comments_count)
VALUES
    (1, 'Just finished a 5-mile run! Feeling great!', NULL, NULL,  '2024-04-07 08:30:00', 3, 2),
    (2, 'Leg day at the gym was intense!', NULL, NULL,'2024-04-06 17:45:00', 1, 1),
    (3, 'Healthy breakfast: oatmeal with fruits and nuts 🥣', NULL, NULL, '2024-04-05 09:00:00', 3, 2),
    (1, 'Completed my first marathon! What an achievement!', NULL, NULL,   '2024-04-04 11:20:00', 0, 0),
    (2, 'Back to the gym after a long break 💪',NULL, NULL,  '2024-04-03 18:00:00', 0, 0);

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

-- Giacomo
INSERT INTO dietplans (idUser, planName, diet)
VALUES
    (2, 'HighCarbs', '{"plan":{"Monday":{"Breakfast":{"Scrambled eggs":150,"Spinach":50,"Whole grain toast":50},"Lunch":{"Quinoa":100,"Mixed vegetables":{"bell peppers":50,"cucumber":50,"tomato":50},"Grilled chicken":150},"Dinner":{"Salmon":150,"Broccoli":100,"Brown rice":100}},"Tuesday":{"Breakfast":{"Greek yogurt":150,"Strawberries":100,"Granola":30},"Lunch":{"Turkey":100,"Avocado":50,"Lettuce":50,"Tomato":50,"Carrot sticks":100},"Dinner":{"Kidney beans":100,"Corn":100,"Bell peppers":50,"Whole grain garlic bread":50}},"Wednesday":{"Breakfast":{"Oatmeal":150,"Banana":100,"Honey":15},"Lunch":{"Grilled vegetables":{"zucchini":100,"eggplant":100,"red onion":50,"roasted red pepper":50},"Salad":150},"Dinner":{"Lean beef":150,"Broccoli":100,"Bell peppers":50,"Snap peas":50,"Brown rice":100}},"Thursday":{"Breakfast":{"Spinach":50,"Banana":100,"Almond milk":150,"Protein powder":30},"Lunch":{"Quinoa":100,"Bell peppers":50,"Black beans":100,"Corn":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chicken breast":150,"Sweet potatoes":150,"Green beans":100}},"Friday":{"Breakfast":{"Whole grain toast":50,"Avocado":50,"Boiled eggs":100},"Lunch":{"Tuna":100,"Mixed greens":100,"Cherry tomatoes":50,"Balsamic vinaigrette dressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snow peas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Whole wheat pancakes":100,"Berries":100,"Maple syrup":30},"Lunch":{"Grilled shrimp":150,"Mixed greens":100,"Cucumber":50,"Citrus vinaigrette dressing":30},"Dinner":{"Spaghetti squash":200,"Turkey meatballs":150,"Asparagus":100}},"Sunday":{"Breakfast":{"Veggie omelette":150,"Mushrooms":50,"Onions":50,"Bell peppers":50,"Cheese":50},"Lunch":{"Lentil soup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Whole grain crackers":50},"Dinner":{"Tilapia":150,"Brussels sprouts":100,"Quinoa pilaf":100}}}}'),
    (1, 'Cheto', '{"plan":{"Monday":{"Breakfast":{"Scrambled eggs":150,"Spinach":50,"Whole grain toast":50},"Lunch":{"Quinoa":100,"Mixed vegetables":{"bell peppers":50,"cucumber":50,"tomato":50},"Grilled chicken":150},"Dinner":{"Salmon":150,"Broccoli":100,"Brown rice":100}},"Tuesday":{"Breakfast":{"Greek yogurt":150,"Strawberries":100,"Granola":30},"Lunch":{"Turkey":100,"Avocado":50,"Lettuce":50,"Tomato":50,"Carrot sticks":100},"Dinner":{"Kidney beans":100,"Corn":100,"Bell peppers":50,"Whole grain garlic bread":50}},"Wednesday":{"Breakfast":{"Oatmeal":150,"Banana":100,"Honey":15},"Lunch":{"Grilled vegetables":{"zucchini":100,"eggplant":100,"red onion":50,"roasted red pepper":50},"Salad":150},"Dinner":{"Lean beef":150,"Broccoli":100,"Bell peppers":50,"Snap peas":50,"Brown rice":100}},"Thursday":{"Breakfast":{"Spinach":50,"Banana":100,"Almond milk":150,"Protein powder":30},"Lunch":{"Quinoa":100,"Bell peppers":50,"Black beans":100,"Corn":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chicken breast":150,"Sweet potatoes":150,"Green beans":100}},"Friday":{"Breakfast":{"Whole grain toast":50,"Avocado":50,"Boiled eggs":100},"Lunch":{"Tuna":100,"Mixed greens":100,"Cherry tomatoes":50,"Balsamic vinaigrette dressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snow peas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Whole wheat pancakes":100,"Berries":100,"Maple syrup":30},"Lunch":{"Grilled shrimp":150,"Mixed greens":100,"Cucumber":50,"Citrus vinaigrette dressing":30},"Dinner":{"Spaghetti squash":200,"Turkey meatballs":150,"Asparagus":100}},"Sunday":{"Breakfast":{"Veggie omelette":150,"Mushrooms":50,"Onions":50,"Bell peppers":50,"Cheese":50},"Lunch":{"Lentil soup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Whole grain crackers":50},"Dinner":{"Tilapia":150,"Brussels sprouts":100,"Quinoa pilaf":100}}}}'),
    (3, 'LowCarbs', '{"plan":{"Monday":{"Breakfast":{"Scrambled eggs":150,"Spinach":50,"Whole grain toast":50},"Lunch":{"Quinoa":100,"Mixed vegetables":{"bell peppers":50,"cucumber":50,"tomato":50},"Grilled chicken":150},"Dinner":{"Salmon":150,"Broccoli":100,"Brown rice":100}},"Tuesday":{"Breakfast":{"Greek yogurt":150,"Strawberries":100,"Granola":30},"Lunch":{"Turkey":100,"Avocado":50,"Lettuce":50,"Tomato":50,"Carrot sticks":100},"Dinner":{"Kidney beans":100,"Corn":100,"Bell peppers":50,"Whole grain garlic bread":50}},"Wednesday":{"Breakfast":{"Oatmeal":150,"Banana":100,"Honey":15},"Lunch":{"Grilled vegetables":{"zucchini":100,"eggplant":100,"red onion":50,"roasted red pepper":50},"Salad":150},"Dinner":{"Lean beef":150,"Broccoli":100,"Bell peppers":50,"Snap peas":50,"Brown rice":100}},"Thursday":{"Breakfast":{"Spinach":50,"Banana":100,"Almond milk":150,"Protein powder":30},"Lunch":{"Quinoa":100,"Bell peppers":50,"Black beans":100,"Corn":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chicken breast":150,"Sweet potatoes":150,"Green beans":100}},"Friday":{"Breakfast":{"Whole grain toast":50,"Avocado":50,"Boiled eggs":100},"Lunch":{"Tuna":100,"Mixed greens":100,"Cherry tomatoes":50,"Balsamic vinaigrette dressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snow peas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Whole wheat pancakes":100,"Berries":100,"Maple syrup":30},"Lunch":{"Grilled shrimp":150,"Mixed greens":100,"Cucumber":50,"Citrus vinaigrette dressing":30},"Dinner":{"Spaghetti squash":200,"Turkey meatballs":150,"Asparagus":100}},"Sunday":{"Breakfast":{"Veggie omelette":150,"Mushrooms":50,"Onions":50,"Bell peppers":50,"Cheese":50},"Lunch":{"Lentil soup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Whole grain crackers":50},"Dinner":{"Tilapia":150,"Brussels sprouts":100,"Quinoa pilaf":100}}}}');

-- Riccardo
INSERT INTO foods (fdnm, kcal, fats, carbohydrates, proteins)
VALUES 
    ('Pollo', 239, 14, 0, 27),
    ('Riso', 130, 0, 28, 3),
    ('Broccoli', 34, 0, 7, 3);

INSERT INTO meal(id_user, meal_date, meal_type, meal)
VALUES
    (1, '2024-10-04', 3, '{"meal":[{"idFood":1, "qty":80},{"idFood":2, "qty":100},{"idFood":3, "qty":300}]}');

-- Kimia
INSERT INTO exercise_category (category_name)
VALUES
    ('Strength'),
    ('Cardio'),
    ('Flexibility'),
    ('Balance');

INSERT INTO exercises(exercise_name, description, exercise_equipment, id_category)
VALUES
    ('Bench Press','this is a description1','Bench',1),
    ('Squat','this is a description1','Barbell',1);

INSERT INTO exercise_plan (idUser, planName, plan)
VALUES
    (1, 'First Plan', '{"plan":{"Monday":[{"idExercise":1,"reps":10,"sets":3,"weight":80},{"idExercise":2,"reps":10,"sets":3,"weight":120}],"Tuesday":[{"idExercise":1,"reps":10,"sets":3,"weight":80},{"idExercise":2,"reps":10,"sets":3,"weight":120}]}}');