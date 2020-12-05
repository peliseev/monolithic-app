-- пароли совпадают с именем пользователя
INSERT INTO USERS (username, password, password_must_be_changed, first_name, last_name, email, address)
VALUES ('admin', '$2a$10$6pBi.3DnarSyecAQCR9PHejFnlpSuIAuX93eNDcU.ycOzYU7RWKF6', false, 'Алексей', 'Алексеев', 'a@gmail.com', 'Украина, Киев'),
       ('customer', '$2a$10$boz8DaebnDv48Fqv4TpPP.aGelSdW1Vln9VRJ3BpPEdLRUagdG/qC', false, 'Борис', 'Борисов', 'b@gmail.com', 'Россия, Москва'),
       ('deliver', '$2a$10$uW2u.mpyfD4HBu0d4wUWSefwzVu6lwWt165YO5ffxKSFA6NF4CGOK', false, 'Валерий', 'Валериев', 'c@gmail.com', 'Россия, Москва'),
       ('supplier', '$2a$10$4vWrlgoX4jD0ujTEqudAyOcdU1xWQ1IrHf7G3tK72DKXtGzDmh1Le', false, 'Генадий', 'Генадиев', 'd@gmail.com', 'Беларусь, Минск');

INSERT INTO ROLES (name) VALUES
    ('ROLE_ADMIN'),
    ('ROLE_CUSTOMER'),
    ('ROLE_DELIVER'),
    ('ROLE_SUPPLIER'),
    ('ROLE_ANONYMOUS');

INSERT INTO USER_ROLES (user_id, role_id)
VALUES (1, 1),
       (2, 2),
       (3, 3),
       (4, 4);

INSERT INTO ITEMS (name, price, quantity) VALUES
    ('Стул', 1000, 12),
    ('Софа', 5000, 20),
    ('Письменный стол', 12000, 10),
    ('Диван', 7000, 7),
    ('Комод', 5000, 10);

