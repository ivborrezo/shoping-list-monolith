--------------------------------
------ONLY FOR H2 DATABASE------
--------------------------------

CREATE SEQUENCE users_user_id_seq
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE users (
    user_id BIGINT default NEXTVAL('users_user_id_seq') PRIMARY KEY,
    user_name VARCHAR NOT NULL,
    email VARCHAR NOT NULL,
    password VARCHAR NOT NULL,
    first_name VARCHAR NOT NULL,
    last_name VARCHAR NOT NULL,
    date_of_birth DATE NOT NULL,
    phone_number VARCHAR,
    CONSTRAINT valid_phone_number CHECK (phone_number ~ '^$|^(\\+[0-9]{1,3})?[0-9]{8,14}$')
);	

CREATE SEQUENCE product_product_id_seq
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE product (
    product_id BIGINT default NEXTVAL('product_product_id_seq') PRIMARY KEY,
    name VARCHAR NOT NULL,
    description TEXT,
    price DOUBLE PRECISION NOT NULL CHECK (price > 0),
    brand VARCHAR NOT NULL,
    grocery_chain VARCHAR,
    user_id BIGINT REFERENCES users(user_id)
);

CREATE SEQUENCE shopping_list_shopping_list_id_seq
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE shopping_list (
    shopping_list_id BIGINT default NEXTVAL('shopping_list_shopping_list_id_seq') PRIMARY KEY,
    name VARCHAR NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    owner_id BIGINT REFERENCES users(user_id)
);

CREATE SEQUENCE category_category_id_seq
	START WITH 1
	INCREMENT BY 1;

CREATE TABLE category (
    category_id BIGINT default NEXTVAL('category_category_id_seq') PRIMARY KEY,
    name VARCHAR NOT NULL,
    color VARCHAR
);

CREATE TABLE list_product (
    shopping_list_id BIGINT,
    product_id BIGINT,
    quantity INTEGER,
    PRIMARY KEY (shopping_list_id, product_id),
    FOREIGN KEY (shopping_list_id) REFERENCES shopping_list(shopping_list_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

CREATE TABLE user_shopping_list_subscription (
    user_id BIGINT REFERENCES users(user_id),
    shopping_list_id BIGINT REFERENCES shopping_list(shopping_list_id),
    PRIMARY KEY (user_id, shopping_list_id)
);

CREATE TABLE product_category (
    product_id BIGINT REFERENCES product(product_id),
    category_id BIGINT REFERENCES category(category_id),
    PRIMARY KEY (product_id, category_id)
);