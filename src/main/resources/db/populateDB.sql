DELETE FROM vote;
DELETE FROM menu_menuitems_link;
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

INSERT INTO menuitem (name, price)
VALUES ('Burger', 450),
       ('Pizza', 800),
       ('Salad', 250),
       ('Dessert', 350),
       ('Soup', 200),
       ('Eggs', 150),
       ('Steak', 900),
       ('Sushi', 750),
       ('Chicken', 400),
       ('Coffee', 100);

INSERT INTO menu (menu_date, restaurant_id)
VALUES (CURRENT_DATE, 100004),
       (CURRENT_DATE, 100005),
       ('2025-03-20', 100006);

INSERT INTO menu_menuitems_link (menu_id, menuitem_id)
VALUES (100017, 100007),
       (100017, 100008),
       (100017, 100009),
       (100017, 100010),
       (100018, 100011),
       (100018, 100012),
       (100019, 100013),
       (100019, 100014),
       (100019, 100010),
       (100019, 100016),
       (100019, 100012);

INSERT INTO vote (vote_date, user_id, restaurant_id)
VALUES (CURRENT_DATE, '100000', '100006'),
       (CURRENT_DATE, '100002', '100004'),
       ('2025-03-20', '100000', '100004'),
       ('2025-03-20', '100001', '100005'),
       ('2025-03-20', '100002', '100006'),
       ('2025-03-21', '100000', '100005'),
       ('2025-03-21', '100001', '100004');
