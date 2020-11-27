INSERT INTO role (name) values
    ('ROLE_USER'),
    ('ROLE_ADMIN'),
    ('ROLE_SUPPLIER');

INSERT INTO customer (username, password, first_name, last_name, email, address)
values ('admin', '$2a$10$9EZ587XD1LtjfZpR6tJ9wecJcDmN12q3tekU3zRajtLkTK0Ve9R.S', 'Остап', 'Бендер', 'ostapbender@mail.ru', 'Украина, Миргород'),
       ('delivery_guy', '$2a$10$9EZ587XD1LtjfZpR6tJ9wecJcDmN12q3tekU3zRajtLkTK0Ve9R.S', 'Киса', 'Воробьянинов', 'kisa@mail.ru', 'Москва');


INSERT INTO CUSTOMER_ROLES(customer_id, role_id)
values  (1, 1),
        (1, 2),
        (2, 3);

INSERT INTO item (name, price, quantity) VALUES
    ('Стул', 1000, 12),
    ('Софа', 5000, 20),
    ('Письменный стол', 12000, 10),
    ('Диван', 7000, 7),
    ('Комод', 5000, 10);

