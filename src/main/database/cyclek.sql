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
    ('Paolo', 'Rossi', '1995-10-08', 'M', 'Paolo95', '123456'),
    ('Alessandro', 'Bianchi', '2000-07-13', 'M', 'ABianchi00', '123456'),
    ('Lucia', 'Verdi', '2002-11-22', 'F', 'Lucy02', '123456');

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
    (1, 'PT Diet', '{"Monday":{"Breakfast":{"Milk":256,"Corn Flakes":60,"apple":150},"Lunch":{"Pasta":180,"Mixedvegetables":{"bellpeppers":50,"cucumber":50,"tomato":50},"Grilledchicken":100},"Dinner":{"Salmon":150,"Broccoli":200,"Bread":220}},"Tuesday":{"Breakfast":{"Milk":256,"Corn Flakes":60,"apple":150},"Lunch":{"Pasta":180,"Mixedvegetables":{"bellpeppers":50,"cucumber":50,"tomato":50},"beans":150},"Dinner":{"Kidneybeans":100,"Corn":100,"Bellpeppers":50,"Wholegraingarlicbread":50}},"Wednesday":{"Breakfast":{"Milk":256,"Corn Flakes":60,"apple":150},"Lunch":{"Grilledvegetables":{"zucchini":100,"eggplant":100,"redonion":50,"roastedredpepper":50},"Salad":150},"Dinner":{"Leanbeef":150,"Broccoli":200,"Brownrice":100}},"Thursday":{"Breakfast":{"Milk":256,"Corn Flakes":60,"apple":150},"Lunch":{"Quinoa":180,"Bellpeppers":50,"Blackbeans":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chickenbreast":150,"Sweetpotatoes":150,"Greenbeans":100}},"Friday":{"Breakfast":{"Milk":256,"Corn Flakes":60,"apple":150},"Lunch":{"Tuna":100,"Mixedgreens":100,"Cherrytomatoes":50,"Balsamicvinaigrettedressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snowpeas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Milk":256,"Corn Flakes":60,"apple":150},"Lunch":{"Grilledshrimp":150,"Mixedgreens":100,"Cucumber":50,"Citrusvinaigrettedressing":30},"Dinner":{"Pizza":350}},"Sunday":{"Breakfast":{"Milk":256,"Corn Flakes":60,"apple":150},"Lunch":{"Lentilsoup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Wholegraincrackers":50},"Dinner":{"Tilapia":150,"Brusselssprouts":100,"Quinoapilaf":100}}}'),
    (1, 'Low Fat', '{"Monday":{"Breakfast":{"Waffles":220,"Blueberries":75,"Pecans":40},"Lunch":{"ChickenSoup":120,"Crackers":45,"CarrotSticks":60,"Celery":45},"Dinner":{"Steak":180,"MashedPotatoes":110,"GreenBeans":90}},"Tuesday":{"Breakfast":{"SmoothieBowl":180,"Strawberries":90,"Banana":60,"AlmondButter":25},"Lunch":{"TunaSandwich":140,"WholegrainBread":60,"Lettuce":55,"Pickles":40},"Dinner":{"VeggieStirFry":160,"BrownRice":95,"Broccoli":70,"Carrots":60,"Peanuts":25}},"Wednesday":{"Breakfast":{"ScrambledEggs":160,"Spinach":70,"Cheese":20},"Lunch":{"GrilledCheese":130,"TomatoSoup":60,"WholegrainCrackers":45},"Dinner":{"Salmon":175,"RoastedVeggies":95,"Asparagus":65,"Zucchini":50,"BellPeppers":35}},"Thursday":{"Breakfast":{"Oatmeal":190,"AppleCinnamon":85,"Walnuts":45},"Lunch":{"ChickpeaSalad":145,"Chickpeas":75,"MixedGreens":65,"Cucumbers":55,"FetaCheese":25},"Dinner":{"TurkeyBreast":170,"MashedSweetPotatoes":105,"GreenSalad":85}},"Friday":{"Breakfast":{"AvocadoToast":120,"WholegrainBread":60,"PoachedEgg":95,"Spinach":40},"Lunch":{"VeggieWrap":150,"WholeWheatTortilla":75,"Hummus":65,"Carrots":55,"Lettuce":40},"Dinner":{"BeanBurritoBowl":180,"BrownRice":115,"BlackBeans":95,"Salsa":75,"Avocado":60}},"Saturday":{"Breakfast":{"FrenchToast":160,"WholewheatBread":85,"Blueberries":70,"MapleSyrup":35},"Lunch":{"GrilledChicken":175,"RoastedPotatoes":100,"MixedVeggies":85},"Dinner":{"PastaPrimavera":210,"WholewheatPasta":125,"Zucchini":95,"BellPeppers":80,"Mushrooms":60}},"Sunday":{"Breakfast":{"BreakfastBurrito":190,"WholeWheatTortilla":110,"ScrambledEggs":75,"Salsa":60,"Avocado":45},"Lunch":{"QuinoaBowl":200,"Quinoa":120,"GrilledVeggies":95,"Broccoli":80,"BellPeppers":65},"Dinner":{"BakedTilapia":160,"WildRice":105,"SteamedAsparagus":90,"Lemon":25}}}'),
    (1, 'High Carbs', '{"Monday":{"Breakfast":{"Scrambledeggs":150,"Spinach":50,"Wholegraintoast":50},"Lunch":{"Quinoa":100,"Mixedvegetables":{"bellpeppers":50,"cucumber":50,"tomato":50},"Grilledchicken":150},"Dinner":{"Salmon":150,"Broccoli":100,"Brownrice":100}},"Tuesday":{"Breakfast":{"Greekyogurt":150,"Strawberries":100,"Granola":30},"Lunch":{"Turkey":100,"Avocado":50,"Lettuce":50,"Tomato":50,"Carrotsticks":100},"Dinner":{"Kidneybeans":100,"Corn":100,"Bellpeppers":50,"Wholegraingarlicbread":50}},"Wednesday":{"Breakfast":{"Oatmeal":150,"Banana":100,"Honey":15},"Lunch":{"Grilledvegetables":{"zucchini":100,"eggplant":100,"redonion":50,"roastedredpepper":50},"Salad":150},"Dinner":{"Leanbeef":150,"Broccoli":100,"Bellpeppers":50,"Snappeas":50,"Brownrice":100}},"Thursday":{"Breakfast":{"Spinach":50,"Banana":100,"Almondmilk":150,"Proteinpowder":30},"Lunch":{"Quinoa":100,"Bellpeppers":50,"Blackbeans":100,"Corn":100,"Tomatoes":50,"Cheese":50},"Dinner":{"Chickenbreast":150,"Sweetpotatoes":150,"Greenbeans":100}},"Friday":{"Breakfast":{"Wholegraintoast":50,"Avocado":50,"Boiledeggs":100},"Lunch":{"Tuna":100,"Mixedgreens":100,"Cherrytomatoes":50,"Balsamicvinaigrettedressing":30},"Dinner":{"Tofu":150,"Broccoli":100,"Carrots":50,"Snowpeas":50,"Quinoa":100}},"Saturday":{"Breakfast":{"Wholewheatpancakes":100,"Berries":100,"Maplesyrup":30},"Lunch":{"Grilledshrimp":150,"Mixedgreens":100,"Cucumber":50,"Citrusvinaigrettedressing":30},"Dinner":{"Spaghettisquash":200,"Turkeymeatballs":150,"Asparagus":100}},"Sunday":{"Breakfast":{"Veggieomelette":150,"Mushrooms":50,"Onions":50,"Bellpeppers":50,"Cheese":50},"Lunch":{"Lentilsoup":200,"Carrots":50,"Celery":50,"Tomatoes":50,"Wholegraincrackers":50},"Dinner":{"Tilapia":150,"Brusselssprouts":100,"Quinoapilaf":100}}}');

-- Riccardo
INSERT INTO foods (fdnm, kcal, fats, carbohydrates, proteins)
VALUES 
    ('Chicken', 239, 14, 0, 27),
    ('Rice', 130, 0, 28, 3),
    ('Greek Yogurt', 93, 1, 3.6, 9),
    ('Milk', 64, 3.6, 4.9, 3.3),
    ('Cereals', 378, 1.1, 87, 6),
    ('Salmon', 185, 12, 3, 18),
    ('Bread', 265, 3.2, 49, 9),
    ('Pasta', 131, 1.1, 25, 5),
    ('Salad', 14, 0.1, 3, 0.9),
    ('Protein Bar', 414, 13, 51, 25),
    ('Broccoli', 34, 0, 7, 3);


INSERT INTO meal(id_user, meal_date, meal_type, meal)
VALUES
    (1, '2024-10-04', 1, '{"meal":[{"idFood":3, "qty":80},{"idFood":4, "qty":100},{"idFood":5, "qty":300}]}'),
    (1, '2024-10-04', 2, '{"meal":[{"idFood":1, "qty":80},{"idFood":2, "qty":100},{"idFood":3, "qty":300}]}'),
    (1, '2024-10-04', 3, '{"meal":[{"idFood":11, "qty":35}]}'),
    (1, '2024-10-04', 4, '{"meal":[{"idFood":6, "qty":120},{"idFood":7, "qty":250}]}');

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