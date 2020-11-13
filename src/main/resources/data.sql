INSERT INTO role (name) values
    ('ROLE_USER'),
    ('ROLE_ADMIN');

INSERT INTO customer (username, password, first_name, last_name, email, address)
values ('admin', '$2a$10$9EZ587XD1LtjfZpR6tJ9wecJcDmN12q3tekU3zRajtLkTK0Ve9R.S', 'Василий', 'Иванов', 'vasya@mail.ru', 'hohoho');

INSERT INTO CUSTOMER_ROLES(customer_id, role_id)
values  (1, 1),
        (1, 2);

INSERT INTO item (name, price, quantity) VALUES
    ('Стул', 300, 2),
    ('Стол', 500, 5),
    ('Шкаф', 300, 1);

