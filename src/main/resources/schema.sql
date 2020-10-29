CREATE TABLE role (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE customer (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	username VARCHAR(20) NOT NULL,
	password VARCHAR(250) NOT NULL,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250) NOT NULL,
	email VARCHAR(250) NOT NULL,
	address VARCHAR(250)
);

CREATE TABLE CUSTOMER_ROLES (
    username VARCHAR(20) NOT NULL,
    role_name VARCHAR(20) NOT NULL,
    foreign key (username) references customer(username),
    foreign key (role_name) references role(name)
);

CREATE TABLE item (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	name VARCHAR(250) NOT NULL,
	price INT NOT NULL,
	quantity INT NOT NULL
);

CREATE TABLE orders (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	customer_id INT NOT NULL,
	status VARCHAR(20) NOT NULL,
	total_price INT NOT NULL,
	foreign key (customer_id) references customer(id)
);

CREATE TABLE order_item (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	order_id VARCHAR(250) NOT NULL,
	item_id VARCHAR(250) NOT NULL,
	count INT NOT NULL,
	foreign key (order_id) references orders(id),
	foreign key (item_id) references item(id)
);