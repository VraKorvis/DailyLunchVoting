DELETE
FROM restaurant;
DELETE
FROM menuitem;
DELETE
FROM menuitem;
ALTER SEQUENCE global_seq RESTART WITH 100000;

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


