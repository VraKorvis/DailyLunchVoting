DELETE
FROM vote;
DELETE
FROM menu_item_assignment;
DELETE
FROM menu_item;
DELETE
FROM menu;
DELETE
FROM restaurant;
DELETE
FROM user_role;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User1', 'user1@gmail.com', '{noop}password'),
       ('User2', 'user2@gmail.com', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO user_role (role, user_id)
VALUES ('USER', 100000),
       ('USER', 100001),
       ('ADMIN', 100002);

INSERT INTO restaurant (name)
VALUES ('Restaurant A'),
       ('Restaurant B'),
       ('Restaurant C');

INSERT INTO menu_item (name /*, price*/)
VALUES ('Burger' /*, 450*/),
       ('Pizza' /*, 800*/),
       ('Salad' /*, 250*/),
       ('Dessert' /*, 350*/),
       ('Soup' /*, 200*/),
       ('Eggs' /*, 150*/),
       ('Steak' /*, 900*/),
       ('Sushi' /*, 750*/),
       ('Chicken' /*, 400*/),
       ('Coffee' /*, 100*/);

INSERT INTO menu (menu_date, restaurant_id)
VALUES (CURRENT_DATE, 100004),
       (CURRENT_DATE, 100005),
       ('2025-03-20', 100006);

INSERT INTO menu_item_assignment (menu_id, menu_item_id, menu_date, name, price)
VALUES (100017, 100007, CURRENT_DATE, 'Burger', 450),
       (100017, 100008, CURRENT_DATE, 'Pizza', 800),
       (100017, 100009, CURRENT_DATE, 'Salad', 250),
       (100017, 100010, CURRENT_DATE, 'Dessert', 350),
       (100018, 100011, CURRENT_DATE, 'Soup', 200),
       (100018, 100012, CURRENT_DATE, 'Eggs', 150),
       (100019, 100013, '2025-03-20', 'Steak', 900),
       (100019, 100014, '2025-03-20', 'Sushi', 750),
       (100019, 100010, '2025-03-20', 'Dessert', 400),
       (100019, 100016, '2025-03-20', 'Coffee', 100),
       (100019, 100012, '2025-03-20', 'Eggs', 150);

INSERT INTO vote (voted_at, user_id, restaurant_id)
VALUES (CURRENT_DATE, '100000', '100005'),
       (CURRENT_DATE, '100002', '100004'),
       ('2025-03-20', '100000', '100004'),
       ('2025-03-20', '100001', '100005'),
       ('2025-03-20', '100002', '100006'),
       ('2025-03-21', '100000', '100005'),
       ('2025-03-21', '100001', '100004');
