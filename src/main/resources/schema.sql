-- Table definition for User
CREATE TABLE users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth TIMESTAMP NOT NULL,
    phone_number VARCHAR(20),
    CONSTRAINT email_unique UNIQUE (email)
);

-- Table definition for ShoppingList
CREATE TABLE shopping_list (
    shopping_list_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    owner_id BIGINT,
    FOREIGN KEY (owner_id) REFERENCES users(user_id)
);

-- Table definition for Product
CREATE TABLE product (
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DOUBLE PRECISION NOT NULL,
    brand VARCHAR(255) NOT NULL,
    grocery_chain VARCHAR(255),
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Table definition for Category
CREATE TABLE category (
    category_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(50)
);

-- Table definition for ListProduct
CREATE TABLE list_product (
    shopping_list_id BIGINT,
    product_id BIGINT,
    quantity INT,
    PRIMARY KEY (shopping_list_id, product_id),
    FOREIGN KEY (shopping_list_id) REFERENCES shopping_list(shopping_list_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);

-- Table definition for user_shopping_list_subscription
CREATE TABLE user_shopping_list_subscription (
    user_id BIGINT,
    shopping_list_id BIGINT,
    PRIMARY KEY (user_id, shopping_list_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (shopping_list_id) REFERENCES shopping_list(shopping_list_id)
);

-- Table definition for product_category
CREATE TABLE product_category (
    product_id BIGINT,
    category_id BIGINT,
    PRIMARY KEY (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id),
    FOREIGN KEY (category_id) REFERENCES category(category_id)
);

-- Inserting sample users
INSERT INTO users (user_name, email, password, first_name, last_name, date_of_birth, phone_number)
VALUES 
    ('john_doe', 'john@example.com', 'password123', 'John', 'Doe', TIMESTAMP('1990-05-15 00:00:00'), '+1234567890'),
    ('jane_smith', 'jane@example.com', 'password456', 'Jane', 'Smith', TIMESTAMP('1985-09-22 00:00:00'), '+1987654321'),
    ('mike_jackson', 'mike@example.com', 'password789', 'Mike', 'Jackson', TIMESTAMP('1978-12-10 00:00:00'), '+1122334455');


-- Inserting sample products
INSERT INTO product (name, description, price, brand, grocery_chain, user_id)
VALUES
    ('Apples', 'Fresh apples from local farm', 2.50, 'Farmers Best', 'Local Grocers', 1),
    ('Milk', 'Organic whole milk', 3.20, 'Organic Farms', 'Super Mart', 1),
    ('Bread', 'Whole wheat bread loaf', 2.00, 'Healthy Grains', 'Bakery King', 2),
    ('Eggs', 'Free-range organic eggs', 4.00, 'Organic Farms', 'Super Mart', 2),
    -- Add more products here...
    ('Bananas', 'Ripe bananas', 1.80, 'Tropical Harvest', 'Local Grocers', 1),
    ('Chicken', 'Fresh chicken breast', 6.50, 'Farmers Poultry', 'Super Mart', 3),
    ('Yogurt', 'Greek yogurt', 3.50, 'Healthy Farms', 'Bakery King', 3),
    -- Add more products here...
    ('Orange Juice', 'Freshly squeezed orange juice', 5.00, 'Juicy Farms', 'Super Mart', 1),
    ('Salmon', 'Wild-caught salmon fillet', 9.00, 'Seafood Delights', 'Local Grocers', 2),
    ('Cheese', 'Imported cheese selection', 7.50, 'International Dairy', 'Super Mart', 2),
    -- Add more products here...
    ('Pasta', 'Whole wheat pasta', 3.00, 'Italian Delights', 'Local Grocers', 3),
    ('Tomatoes', 'Vine-ripened tomatoes', 2.20, 'Fresh Farms', 'Super Mart', 1),
    ('Beef', 'Grass-fed beef steak', 8.00, 'Pasture Perfect', 'Local Grocers', 1),
    -- Add more products here...
    ('Cereal', 'Whole grain cereal', 4.50, 'Morning Delights', 'Super Mart', 2),
    ('Spinach', 'Organic baby spinach', 2.80, 'Green Fields', 'Local Grocers', 3),
    ('Potatoes', 'Russet potatoes', 2.00, 'Harvest Farms', 'Super Mart', 3),
    -- Add more products here...
    ('Soy Milk', 'Organic soy milk', 3.50, 'Bean Fields', 'Local Grocers', 1),
    ('Applesauce', 'Natural applesauce', 2.80, 'Fruit Blends', 'Super Mart', 2),
    ('Ice Cream', 'Vanilla bean ice cream', 6.00, 'Creamy Treats', 'Local Grocers', 2);

-- Inserting sample shopping lists
INSERT INTO shopping_list (name, creation_date, owner_id)
VALUES
    ('Grocery Shopping', TIMESTAMP('2024-03-20 00:00:00'), 1),
    ('Weekly Essentials', TIMESTAMP('2024-03-21 00:00:00'), 2),
    ('Family BBQ', TIMESTAMP('2024-03-22 00:00:00'), 1),
    ('Dinner Party', TIMESTAMP('2024-03-23 00:00:00'), 3),
    ('Healthy Eating', TIMESTAMP('2024-03-24 00:00:00'), 2);

-- Inserting sample categories
INSERT INTO category (name, color)
VALUES
    ('Fruits', 'RED'),
    ('Dairy', 'WHITE'),
    ('Bakery', 'BROWN'),
    ('Poultry', 'YELLOW'),
    ('Vegetables', 'GREEN'),
    ('Yogurt', 'WHITE'),
    ('Seafood', 'BLUE'),
    ('Cheese', 'YELLOW'),
    ('Pasta', 'BROWN'),
    ('Cereal', 'ORANGE'),
    ('Leafy Greens', 'GREEN'),
    ('Meat', 'RED');

-- Inserting sample list_product relations
INSERT INTO list_product (shopping_list_id, product_id, quantity)
VALUES
    (1, 1, 3),
    (1, 2, 2),
    (1, 5, 1),
    (2, 3, 1),
    (2, 4, 2),
    -- Add more list_product relations here...
    (3, 6, 1),
    (3, 7, 2),
    (3, 8, 1),
    -- Add more list_product relations here...
    (4, 9, 1),
    (4, 10, 2),
    (4, 11, 1),
    -- Add more list_product relations here...
    (5, 12, 3),
    (5, 13, 1),
    (5, 14, 2);

-- Inserting sample product_category relations
INSERT INTO product_category (product_id, category_id)
VALUES
    (1, 1),
    (2, 2),
    (3, 3),
    (4, 4),
    -- Add more product_category relations here...
    (5, 1),
    (6, 5),
    (7, 6),
    -- Add more product_category relations here...
    (8, 2),
    (9, 7),
    (10, 8),
    -- Add more product_category relations here...
    (11, 3),
    (12, 9),
    (13, 10),
    -- Add more product_category relations here...
    (14, 4),
    (15, 11),
    (16, 12);

