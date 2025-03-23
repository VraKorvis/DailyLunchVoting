DELETE FROM vote;
DELETE FROM restaurant_menu_history;
DELETE FROM menu_menuitem_link;
DELETE FROM menuitem;
DELETE FROM menu;
DELETE FROM restaurant;
DELETE FROM user_role;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User1', 'user1@yandex.ru', 'password'),
       ('User2', 'user2@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('ADMIN', 100002);

INSERT INTO restaurant (name)
VALUES ('Restaurant A'),
       ('Restaurant B'),
       ('Restaurant C');

INSERT INTO vote (date_time, user_id, restaurant_id)
VALUES ('2025-03-20 07:00:00', '100000', '100004'),
       ('2025-03-20 08:00:00', '100001', '100005'),
       ('2025-03-20 09:00:00', '100002', '100006'),
       ('2025-03-21 08:00:00', '100000', '100005'),
       ('2025-03-21 09:00:00', '100001', '100004'),
       ('2025-03-21 10:00:00', '100002', '100006');

INSERT INTO menuitem (name, price)
VALUES ('Burger', 300),
       ('Pizza', 800),
       ('Salad', 200),
       ('Dessert', 450),
       ('Soup', 150),
       ('eggs', '300'),
       ('steak', '400'),
       ('shi', '500');


