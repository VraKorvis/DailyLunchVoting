DROP TABLE IF EXISTS vote;
DROP TABLE IF EXISTS menu_menuitems_link;
DROP TABLE IF EXISTS menuitem;
DROP TABLE IF EXISTS menu;
DROP TABLE IF EXISTS restaurant;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS users;
DROP SEQUENCE IF EXISTS global_seq;

CREATE SEQUENCE global_seq START WITH 100000;

CREATE TABLE users (
                       id INTEGER DEFAULT NEXT VALUE FOR global_seq PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       registered TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                       enabled BOOLEAN DEFAULT TRUE NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON users (email);

CREATE TABLE user_role (
                           user_id INTEGER NOT NULL,
                           role VARCHAR(255) NOT NULL,
                           enabled BOOLEAN DEFAULT TRUE NOT NULL,
                           CONSTRAINT user_roles_idx UNIQUE (user_id, role),
                           FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE restaurant (
                            id INTEGER DEFAULT NEXT VALUE FOR global_seq PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            CONSTRAINT restaurant_name_unique UNIQUE (name)
);

CREATE TABLE vote (
                      id INTEGER DEFAULT NEXT VALUE FOR global_seq PRIMARY KEY,
                      vote_date DATE DEFAULT CURRENT_DATE NOT NULL,
                      user_id INTEGER NOT NULL,
                      restaurant_id INTEGER NOT NULL,
                      FOREIGN KEY (user_id) REFERENCES users (id),
                      FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE
);
CREATE UNIQUE INDEX vote_unique_date_user_idx ON vote (vote_date, user_id);

CREATE TABLE menu (
                      id INTEGER DEFAULT NEXT VALUE FOR global_seq PRIMARY KEY,
                      menu_date DATE DEFAULT CURRENT_DATE NOT NULL,
                      restaurant_id INTEGER NOT NULL,
                      FOREIGN KEY (restaurant_id) REFERENCES restaurant (id) ON DELETE CASCADE,
                      CONSTRAINT unique_menu_per_day UNIQUE (restaurant_id, menu_date)
);

CREATE TABLE menuitem (
                          id INTEGER DEFAULT NEXT VALUE FOR global_seq PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          price DECIMAL(10,2) DEFAULT 500 NOT NULL,
                          CONSTRAINT menuitem_unique UNIQUE (name, price)
);

CREATE TABLE menu_menuitems_link (
                                     menu_id INTEGER NOT NULL,
                                     menuitem_id INTEGER NOT NULL,
                                     PRIMARY KEY (menu_id, menuitem_id),
                                     FOREIGN KEY (menu_id) REFERENCES menu (id),
                                     FOREIGN KEY (menuitem_id) REFERENCES menuitem (id)
);
