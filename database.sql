
DROP DATABASE IF EXISTS ecommerce;
CREATE DATABASE IF NOT EXISTS ecommerce;
USE ecommerce;

-- ==========================================
-- 1. TẠO CÁC BẢNG (TABLES)
-- ==========================================

CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       email VARCHAR(255),
                       phone VARCHAR(20),
                       password VARCHAR(255),
                       name VARCHAR(255),
                       dob DATE,
                       address VARCHAR(400),
                       avatar VARCHAR(255),
                       gender VARCHAR(10),
                       role_id INT,
                       is_active BOOLEAN DEFAULT TRUE,
                       created_at DATETIME,
                       updated_at DATETIME
);

CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50)
);

CREATE TABLE social_accounts (
                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                 user_id INT,
                                 provider_id VARCHAR(255),
                                 provider_name VARCHAR(50),
                                 email VARCHAR(255),
                                 created_at DATETIME,
                                 updated_at DATETIME
);

CREATE TABLE categories (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(255),
                            created_at DATETIME,
                            updated_at DATETIME
);

CREATE TABLE products (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255),
                          description TEXT,
                          thumbnail VARCHAR(255),
                          sold_quantity INT,
                          rating DECIMAL(2,1),
                          rating_count INT,
                          min_price DECIMAL(12,2),
                          max_price DECIMAL(12,2),
                          is_hot BOOLEAN,
                          total_stock INT,
                          category_id INT,
                          status VARCHAR(50),
                          created_at DATETIME,
                          updated_at DATETIME
);

CREATE TABLE product_variants (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  product_id INT,
                                  sku VARCHAR(100) UNIQUE,
                                  original_price DECIMAL(12,2),
                                  price DECIMAL(12,2),
                                  stock INT,
                                  image VARCHAR(255),
                                  created_at DATETIME,
                                  updated_at DATETIME
);

CREATE TABLE attributes (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            name VARCHAR(100),
                            created_at DATETIME,
                            updated_at DATETIME
);

CREATE TABLE attribute_values (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  attribute_id INT,
                                  value VARCHAR(100),
                                  created_at DATETIME,
                                  updated_at DATETIME
);

CREATE TABLE variant_attribute_values (
                                          variant_id INT,
                                          attribute_value_id INT,
                                          created_at DATETIME,
                                          updated_at DATETIME,
                                          PRIMARY KEY (variant_id, attribute_value_id)
);

CREATE TABLE feedbacks (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           product_id INT,
                           user_id INT,
                           star INT,
                           content TEXT,
                           created_at DATETIME,
                           updated_at DATETIME
);

CREATE TABLE product_images (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                product_id INT,
                                image_url VARCHAR(255),
                                created_at DATETIME,
                                updated_at DATETIME
);

CREATE TABLE coupons (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         code VARCHAR(50) UNIQUE,
                         discount_type VARCHAR(50),
                         discount_value DECIMAL(12,2),
                         min_order_value INT,
                         max_discount INT,
                         usage_limit INT,
                         used_count INT,
                         start_date DATETIME,
                         end_date DATETIME,
                         status VARCHAR(50),
                         created_at DATETIME,
                         updated_at DATETIME
);

CREATE TABLE orders (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT,
                        coupon_id INT,
                        note VARCHAR(255),
                        total DECIMAL(12,2),
                        status VARCHAR(50),
                        created_at DATETIME,
                        updated_at DATETIME
);

CREATE TABLE payments (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          order_id INT UNIQUE, -- One-to-One
                          transaction_id VARCHAR(255),
                          payment_method VARCHAR(50),
                          payment_status VARCHAR(50),
                          created_at DATETIME,
                          updated_at DATETIME
);

CREATE TABLE shipping_details (
                                  id INT AUTO_INCREMENT PRIMARY KEY,
                                  order_id INT UNIQUE, -- One-to-One
                                  name VARCHAR(255),
                                  phone VARCHAR(20),
                                  email VARCHAR(255),
                                  gender VARCHAR(10),
                                  shipping_address VARCHAR(255),
                                  shipping_method VARCHAR(50),
                                  carrier VARCHAR(100),
                                  created_at DATETIME,
                                  updated_at DATETIME
);

CREATE TABLE order_details (
                               id INT AUTO_INCREMENT PRIMARY KEY,
                               order_id INT,
                               variant_id INT,
                               price DECIMAL(12,2),
                               quantity INT,
                               created_at DATETIME,
                               updated_at DATETIME
);

CREATE TABLE carts (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       user_id INT UNIQUE, -- One-to-One
                       created_at DATETIME,
                       updated_at DATETIME
);

CREATE TABLE cart_items (
                            id INT AUTO_INCREMENT PRIMARY KEY,
                            cart_id INT,
                            variant_id INT,
                            quantity INT,
                            created_at DATETIME,
                            updated_at DATETIME
);


-- ==========================================
-- 2. THIẾT LẬP KHÓA NGOẠI (FOREIGN KEYS)
-- ==========================================
-- USERS -> ROLES
ALTER TABLE users
    ADD CONSTRAINT fk_users_roles
        FOREIGN KEY (role_id) REFERENCES roles(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- SOCIAL ACCOUNTS -> USERS
ALTER TABLE social_accounts
    ADD CONSTRAINT fk_social_accounts_users
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- PRODUCTS -> CATEGORIES
ALTER TABLE products
    ADD CONSTRAINT fk_products_categories
        FOREIGN KEY (category_id) REFERENCES categories(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- PRODUCT_VARIANTS -> PRODUCTS
ALTER TABLE product_variants
    ADD CONSTRAINT fk_variants_products
        FOREIGN KEY (product_id) REFERENCES products(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- ATTRIBUTE_VALUES -> ATTRIBUTES
ALTER TABLE attribute_values
    ADD CONSTRAINT fk_attribute_values_attributes
        FOREIGN KEY (attribute_id) REFERENCES attributes(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- VARIANT_ATTRIBUTE_VALUES (N-N)
ALTER TABLE variant_attribute_values
    ADD CONSTRAINT fk_vav_variant
        FOREIGN KEY (variant_id) REFERENCES product_variants(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE variant_attribute_values
    ADD CONSTRAINT fk_vav_attribute_value
        FOREIGN KEY (attribute_value_id) REFERENCES attribute_values(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- FEEDBACKS
ALTER TABLE feedbacks
    ADD CONSTRAINT fk_feedbacks_products
        FOREIGN KEY (product_id) REFERENCES products(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE feedbacks
    ADD CONSTRAINT fk_feedbacks_users
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- PRODUCT IMAGES
ALTER TABLE product_images
    ADD CONSTRAINT fk_product_images_products
        FOREIGN KEY (product_id) REFERENCES products(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- ORDERS
ALTER TABLE orders
    ADD CONSTRAINT fk_orders_users
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

ALTER TABLE orders
    ADD CONSTRAINT fk_orders_coupons
        FOREIGN KEY (coupon_id) REFERENCES coupons(id)
            ON DELETE SET NULL
            ON UPDATE CASCADE;

-- PAYMENTS (1-1)
ALTER TABLE payments
    ADD CONSTRAINT fk_payments_orders
        FOREIGN KEY (order_id) REFERENCES orders(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- SHIPPING DETAILS (1-1)
ALTER TABLE shipping_details
    ADD CONSTRAINT fk_shipping_orders
        FOREIGN KEY (order_id) REFERENCES orders(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- ORDER DETAILS
ALTER TABLE order_details
    ADD CONSTRAINT fk_order_details_orders
        FOREIGN KEY (order_id) REFERENCES orders(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE order_details
    ADD CONSTRAINT fk_order_details_variants
        FOREIGN KEY (variant_id) REFERENCES product_variants(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- CARTS (1-1 USERS)
ALTER TABLE carts
    ADD CONSTRAINT fk_carts_users
        FOREIGN KEY (user_id) REFERENCES users(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

-- CART ITEMS
ALTER TABLE cart_items
    ADD CONSTRAINT fk_cart_items_carts
        FOREIGN KEY (cart_id) REFERENCES carts(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;

ALTER TABLE cart_items
    ADD CONSTRAINT fk_cart_items_variants
        FOREIGN KEY (variant_id) REFERENCES product_variants(id)
            ON DELETE CASCADE
            ON UPDATE CASCADE;