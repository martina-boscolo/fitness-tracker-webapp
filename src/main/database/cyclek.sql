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
CREATE TABLE bodyStats
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

CREATE TABLE bodyObjective
(
    id          SERIAL PRIMARY KEY,
    idUser      INTEGER NOT NULL REFERENCES users (id),
    weight      FLOAT NOT NULL,
    height      FLOAT NOT NULL,
    fatty       FLOAT NOT NULL,
    lean        FLOAT NOT NULL,
    objDate     DATE DEFAULT CURRENT_DATE,

    CONSTRAINT unique_daily_obj UNIQUE (idUser, objDate)
);

-- Martina
CREATE TABLE posts
(
    id              SERIAL PRIMARY KEY,
    id_user         INTEGER NOT NULL REFERENCES users (id),
    text_content    TEXT NOT NULL,
    image_path      TEXT, -- images should be stored in the filesystem
    like_count      INTEGER NOT NULL DEFAULT 0,
    comment_count   INTEGER NOT NULL DEFAULT 0,
    post_date       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE likes
(
    id          SERIAL PRIMARY KEY,
    id_user     INTEGER NOT NULL REFERENCES users (id),
    id_post     INTEGER NOT NULL REFERENCES posts (id),
    is_like     BOOLEAN NOT NULL,

    CONSTRAINT unique_post_user_combination UNIQUE (id_post, id_user) -- only one like on the same post by the same user
);

CREATE TABLE comments
(
    id           SERIAL PRIMARY KEY,
    id_user      INTEGER NOT NULL REFERENCES users (id),
    id_post      INTEGER NOT NULL REFERENCES posts (id),
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

CREATE TABLE exercise (
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
    ('Paolo', 'Rossi', '1995-10-08', 'M', 'user1', '1234'),
    ('Paolo', 'Bianchi', '1995-10-08', 'M', 'user2', '1234'),
    ('Lucia', 'Rossi', '1995-10-08', 'F', 'user3', '1234');

-- Alessio
INSERT INTO bodyStats (idUser, weight, height, fatty, lean, statsDate)
VALUES
    (1, 82, 175, 14.8, 20, '2024-01-02 08:30:00'),
    (2, 65.8, 190, 14, 30.6, '2024-04-03 16:12:00'),
    (3, 82, 165, 27.2, 28.6, '2024-02-07 12:30:00'),
    (3, 80, 165, 2.2, 29, '2024-02-11 12:30:00'),
    (1, 78, 175, 13.6, 23.5, '2024-04-09 09:43:00');

INSERT INTO bodyObjective (idUser, weight, height, fatty, lean, objDate)
VALUES
    (1, 75, 175, 10, 25, '2024-04-09 09:43:00'),
    (2, 65.8, 190, 14, 30.6, '2024-04-03 16:12:00'),
    (3, 80, 165, 2.2, 29, '2024-02-11 12:30:00');

-- Martina
INSERT INTO posts (id_user, text_content, image_path, like_count, comment_count, post_date)
VALUES
    (1, 'Just finished a 5-mile run! Feeling great!', '/fitness/images/run.jpg', 10, 2, '2024-04-07 08:30:00'),
    (2, 'Leg day at the gym was intense!', '/fitness/images/legday.jpg', 15, 1, '2024-04-06 17:45:00'),
    (3, 'Healthy breakfast: oatmeal with fruits and nuts 🥣', NULL, 20, 0, '2024-04-05 09:00:00'),
    (1, 'Completed my first marathon! What an achievement!', '/fitness/images/marathon.jpg', 50, 3, '2024-04-04 11:20:00'),
    (2, 'Back to the gym after a long break 💪', '/fitness/images/gym.jpg', 30, 5, '2024-04-03 18:00:00');


INSERT INTO likes (id_user, id_post, is_like)
VALUES
    (1, 1, TRUE),
    (2, 1, TRUE),
    (3, 1, TRUE),
    (1, 2, TRUE),
    (3, 3, TRUE),
    (1, 3, FALSE),
    (2, 3, FALSE);

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
    (2, 'HighCarbs', '{"Monday":{"Breakfast":{"Scrambled eggs":150,"Spinach":50,"Whole grain toast":50},"Lunch":{"Quinoa":100,"Mixed vegetables":{"bell peppers":50,"cucumber":50,"tomato":50},"Grilled chicken":150},"Dinner":{"Salmon":150,"Broccoli":100,"Brown rice":100}},"Tuesday":{"Breakfast":{"Greek yogurt":150,"Strawberries":100,"Granola":30},"Lunch":{"Turkey":100,"Avocado":50,"Lettuce":50,"Tomato":50,"Carrot sticks":100},"Dinner":{"Kidney beans":100,"Corn":100,"Bell peppers":50,"Whole grain garlic bread":50}},"Wednesday":{"Breakfast":{"Oatmeal":150,"Banana":100,"Honey":15},"Lunch":{"Grilled vegetables":{"zucchini":100,"eggplant":100,"red onion":50,"roasted red pepper":50},"Salad":150},"Dinner":{"Lean beef":150,"Broccoli":100,"Bell peppers":50,"Snap peas":50,"Brown rice":100}},"Thursday":{"Breakfast":{"Spinach":50,"Banana":100,"Almond milk":150,"Protein powder":30},"Lunch":{"Quinoa":100,"Bell peppers":50,"Black beans":100,"Corn":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chicken breast":150,"Sweet potatoes":150,"Green beans":100}},"Friday":{"Breakfast":{"Whole grain toast":50,"Avocado":50,"Boiled eggs":100},"Lunch":{"Tuna":100,"Mixed greens":100,"Cherry tomatoes":50,"Balsamic vinaigrette dressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snow peas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Whole wheat pancakes":100,"Berries":100,"Maple syrup":30},"Lunch":{"Grilled shrimp":150,"Mixed greens":100,"Cucumber":50,"Citrus vinaigrette dressing":30},"Dinner":{"Spaghetti squash":200,"Turkey meatballs":150,"Asparagus":100}},"Sunday":{"Breakfast":{"Veggie omelette":150,"Mushrooms":50,"Onions":50,"Bell peppers":50,"Cheese":50},"Lunch":{"Lentil soup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Whole grain crackers":50},"Dinner":{"Tilapia":150,"Brussels sprouts":100,"Quinoa pilaf":100}}}'),
    (1, 'Cheto', '{"Monday":{"Breakfast":{"Scrambled eggs":150,"Spinach":50,"Whole grain toast":50},"Lunch":{"Quinoa":100,"Mixed vegetables":{"bell peppers":50,"cucumber":50,"tomato":50},"Grilled chicken":150},"Dinner":{"Salmon":150,"Broccoli":100,"Brown rice":100}},"Tuesday":{"Breakfast":{"Greek yogurt":150,"Strawberries":100,"Granola":30},"Lunch":{"Turkey":100,"Avocado":50,"Lettuce":50,"Tomato":50,"Carrot sticks":100},"Dinner":{"Kidney beans":100,"Corn":100,"Bell peppers":50,"Whole grain garlic bread":50}},"Wednesday":{"Breakfast":{"Oatmeal":150,"Banana":100,"Honey":15},"Lunch":{"Grilled vegetables":{"zucchini":100,"eggplant":100,"red onion":50,"roasted red pepper":50},"Salad":150},"Dinner":{"Lean beef":150,"Broccoli":100,"Bell peppers":50,"Snap peas":50,"Brown rice":100}},"Thursday":{"Breakfast":{"Spinach":50,"Banana":100,"Almond milk":150,"Protein powder":30},"Lunch":{"Quinoa":100,"Bell peppers":50,"Black beans":100,"Corn":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chicken breast":150,"Sweet potatoes":150,"Green beans":100}},"Friday":{"Breakfast":{"Whole grain toast":50,"Avocado":50,"Boiled eggs":100},"Lunch":{"Tuna":100,"Mixed greens":100,"Cherry tomatoes":50,"Balsamic vinaigrette dressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snow peas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Whole wheat pancakes":100,"Berries":100,"Maple syrup":30},"Lunch":{"Grilled shrimp":150,"Mixed greens":100,"Cucumber":50,"Citrus vinaigrette dressing":30},"Dinner":{"Spaghetti squash":200,"Turkey meatballs":150,"Asparagus":100}},"Sunday":{"Breakfast":{"Veggie omelette":150,"Mushrooms":50,"Onions":50,"Bell peppers":50,"Cheese":50},"Lunch":{"Lentil soup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Whole grain crackers":50},"Dinner":{"Tilapia":150,"Brussels sprouts":100,"Quinoa pilaf":100}}}'),
    (3, 'LowCarbs', '{"Monday":{"Breakfast":{"Scrambled eggs":150,"Spinach":50,"Whole grain toast":50},"Lunch":{"Quinoa":100,"Mixed vegetables":{"bell peppers":50,"cucumber":50,"tomato":50},"Grilled chicken":150},"Dinner":{"Salmon":150,"Broccoli":100,"Brown rice":100}},"Tuesday":{"Breakfast":{"Greek yogurt":150,"Strawberries":100,"Granola":30},"Lunch":{"Turkey":100,"Avocado":50,"Lettuce":50,"Tomato":50,"Carrot sticks":100},"Dinner":{"Kidney beans":100,"Corn":100,"Bell peppers":50,"Whole grain garlic bread":50}},"Wednesday":{"Breakfast":{"Oatmeal":150,"Banana":100,"Honey":15},"Lunch":{"Grilled vegetables":{"zucchini":100,"eggplant":100,"red onion":50,"roasted red pepper":50},"Salad":150},"Dinner":{"Lean beef":150,"Broccoli":100,"Bell peppers":50,"Snap peas":50,"Brown rice":100}},"Thursday":{"Breakfast":{"Spinach":50,"Banana":100,"Almond milk":150,"Protein powder":30},"Lunch":{"Quinoa":100,"Bell peppers":50,"Black beans":100,"Corn":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chicken breast":150,"Sweet potatoes":150,"Green beans":100}},"Friday":{"Breakfast":{"Whole grain toast":50,"Avocado":50,"Boiled eggs":100},"Lunch":{"Tuna":100,"Mixed greens":100,"Cherry tomatoes":50,"Balsamic vinaigrette dressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snow peas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Whole wheat pancakes":100,"Berries":100,"Maple syrup":30},"Lunch":{"Grilled shrimp":150,"Mixed greens":100,"Cucumber":50,"Citrus vinaigrette dressing":30},"Dinner":{"Spaghetti squash":200,"Turkey meatballs":150,"Asparagus":100}},"Sunday":{"Breakfast":{"Veggie omelette":150,"Mushrooms":50,"Onions":50,"Bell peppers":50,"Cheese":50},"Lunch":{"Lentil soup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Whole grain crackers":50},"Dinner":{"Tilapia":150,"Brussels sprouts":100,"Quinoa pilaf":100}}}');

-- Riccardo
INSERT INTO foods (fdnm, kcal, fats, carbohydrates, proteins)
VALUES 
    ('Pollo', 239, 14, 0, 27),
    ('Riso', 130, 0, 28, 3),
    ('Broccoli', 34, 0, 7, 3);

INSERT INTO meal(id_user, meal_date, meal_type, meal)
VALUES
    (1, '2024-10-04', 3, '{"meal":[{"id_food":1, "grams":80},{"id_food":2, "grams":100},{"id_food":3, "grams":300}]}');

-- Kimia
INSERT INTO exercise_category (category_name)
VALUES
    ('Strength'),
    ('Cardio'),
    ('Flexibility'),
    ('Balance');

INSERT INTO exercise(exercise_name, description, exercise_equipment, id_category)
VALUES
    ('squat','this is a description1','Barbell',1);

INSERT INTO exercise_plan (idUser, planName, plan)
VALUES
    (1, 'First Plan', '{"Monday":[{"idExercise":1,"reps":10,"sets":3,"weight":50},{"idExercise":1,"reps":10,"sets":3,"weight":50},{"idExercise":1,"reps":10,"sets":3,"weight":50}],"Tuesday":[{"idExercise":1,"reps":10,"sets":3,"weight":50},{"idExercise":1,"reps":10,"sets":3,"weight":50},{"idExercise":1,"reps":10,"sets":3,"weight":50}]}');