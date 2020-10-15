CREATE TABLE address (
    id INT AUTO_INCREMENT  PRIMARY KEY,
    city varchar(250) NOT NULL,
    country VARCHAR(250) NOT NULL,
    house VARCHAR(250) NOT NULL,
    flat VARCHAR(250) NOT NULL
);

CREATE TABLE customer (
	id INT AUTO_INCREMENT  PRIMARY KEY,
	first_name VARCHAR(250) NOT NULL,
	last_name VARCHAR(250) NOT NULL,
	email VARCHAR(250) NOT NULL,
	address_id INT,
	foreign key (address_id) references address(id)
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