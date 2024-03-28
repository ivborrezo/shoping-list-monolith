--------------------------------
------ONLY FOR H2 DATABASE------
--------------------------------

-- Insert examples into the users table
INSERT INTO users (user_name, email, password, first_name, last_name, date_of_birth, phone_number)
VALUES 
    ('john_doe', 'john@example.com', 'password123', 'John', 'Doe', '1990-05-15', '234567890'),
    ('jane_smith', 'jane@example.com', 'password456', 'Jane', 'Smith', '1985-08-20', '987654321'),
    ('mike_jackson', 'mike@example.com', 'password789', 'Mike', 'Jackson', '1982-03-10', '765432890'),
    ('sarah_adams', 'sarah@example.com', 'password321', 'Sarah', 'Adams', '1995-11-25', '928374650'),
    ('david_brown', 'david@example.com', 'password654', 'David', 'Brown', '1978-09-30', '346798520');

-- Insert examples into the product table
INSERT INTO product (name, description, price, brand, grocery_chain, user_id)
VALUES 
    ('Smartphone', 'Latest smartphone model', 899.99, 'Samsung', 'Best Mart', 1),
    ('Laptop', 'High-performance laptop', 1299.99, 'Apple', 'Tech World', 2),
    ('Headphones', 'Noise-canceling headphones', 199.99, 'Sony', 'Sound Express', 3),
    ('Smartwatch', 'Fitness tracking smartwatch', 299.99, 'Fitbit', 'Gadget Zone', 4),
    ('Tablet', 'Portable tablet device', 499.99, 'Microsoft', 'Electronics Hub', 5);

-- Insert examples into the shopping_list table
INSERT INTO shopping_list (name, creation_date, owner_id)
VALUES 
    ('Grocery List', '2024-03-25 10:00:00', 1),
    ('Tech Gadgets', '2024-03-26 15:30:00', 2),
    ('Fitness Gear', '2024-03-27 09:45:00', 3),
    ('Home Essentials', '2024-03-28 11:20:00', 4),
    ('Travel Packing', '2024-03-29 14:00:00', 5);

-- Insert examples into the category table
INSERT INTO category (name, color)
VALUES 
    ('Electronics', 'Blue'),
    ('Groceries', 'Green'),
    ('Gadgets', 'Red'),
    ('Fitness', 'Yellow'),
    ('Home', 'Purple');
   
-- Insert examples into the product_category table
INSERT INTO product_category  (product_id , category_id)
VALUES 
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (5, 1);

-- Insert examples into the list_product table
INSERT INTO list_product (shopping_list_id, product_id, quantity)
VALUES 
    (1, 5, 3),
    (2, 1, 1),
    (3, 3, 2),
    (4, 2, 1),
    (5, 4, 2);

-- Insert examples into the user_shopping_list_subscription table
INSERT INTO user_shopping_list_subscription (user_id, shopping_list_id)
VALUES 
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (5, 1);