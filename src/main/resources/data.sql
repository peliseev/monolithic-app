INSERT INTO ROLES (name) VALUES
    ('ROLE_USER'),
    ('ROLE_ADMIN'),
    ('ROLE_DELIVERY'),
    ('ROLE_SUPPLIER');

INSERT INTO CUSTOMERS (username, password, first_name, last_name, email, address)
VALUES ('admin', '$2a$10$9EZ587XD1LtjfZpR6tJ9wecJcDmN12q3tekU3zRajtLkTK0Ve9R.S', 'Остап', 'Бендер', 'ostapbender@mail.ru', 'Украина, Миргород'),
       ('delivery_guy', '$2a$10$9EZ587XD1LtjfZpR6tJ9wecJcDmN12q3tekU3zRajtLkTK0Ve9R.S', 'Киса', 'Воробьянинов', 'kisa@mail.ru', 'Москва'),
       ('supplier', '$2a$10$9EZ587XD1LtjfZpR6tJ9wecJcDmN12q3tekU3zRajtLkTK0Ve9R.S', 'Эллочка-людоедка', 'Щукина', 'ella@mail.ru', 'Москва, Дом с кариатидами');

INSERT INTO USER_ROLES (user_id, role_id)
VALUES (1, 2),
       (2, 3),
       (3, 4);

INSERT INTO ITEMS (name, price, quantity) VALUES
    ('Стул', 1000, 12),
    ('Софа', 5000, 20),
    ('Письменный стол', 12000, 10),
    ('Диван', 7000, 7),
    ('Комод', 5000, 10);

