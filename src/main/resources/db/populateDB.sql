DELETE FROM vote;
DELETE FROM menu_history;
DELETE FROM menu_menuitem;
DELETE FROM menuitem;
DELETE FROM menu;
DELETE FROM restaurant;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

INSERT INTO restaurant (name)
VALUES ('Restaurant A'),
       ('Restaurant B'),
       ('Restaurant C');

-- Заполнение таблицы menuitem с английскими названиями
INSERT INTO menuitem (name, price)
VALUES ('Burger', 300),
       ('Pizza', 800),
       ('Salad', 200),
       ('Dessert', 450),
       ('Soup', 150),
       ('eggs', '300'),
       ('steik', '400'),
       ('shi', '500');


