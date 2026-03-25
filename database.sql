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
                       avatar VARCHAR(255),
                       status VARCHAR(50),
                       created_at DATETIME,
                       updated_at DATETIME
);

CREATE TABLE roles (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50)
);

CREATE TABLE user_roles (
                            user_id INT,
                            role_id INT,
                            PRIMARY KEY (user_id, role_id)
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
                                  price_cost DECIMAL(12,2),
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
                                  shipping_address VARCHAR(255),
                                  shipping_method VARCHAR(50),
                                  carrier VARCHAR(100),
                                  shipping_fee DECIMAL(12,2),
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

-- Quyền & Tài khoản
ALTER TABLE user_roles ADD CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE user_roles ADD CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE;
ALTER TABLE social_accounts ADD CONSTRAINT fk_sa_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Sản phẩm & Biến thể
ALTER TABLE products ADD CONSTRAINT fk_prod_cat FOREIGN KEY (category_id) REFERENCES categories(id);
ALTER TABLE product_images ADD CONSTRAINT fk_pi_prod FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE;
ALTER TABLE product_variants ADD CONSTRAINT fk_pv_prod FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE;
ALTER TABLE attribute_values ADD CONSTRAINT fk_av_attr FOREIGN KEY (attribute_id) REFERENCES attributes(id) ON DELETE CASCADE;

-- Many-to-Many: Variant - Attribute Value
ALTER TABLE variant_attribute_values ADD CONSTRAINT fk_vav_variant FOREIGN KEY (variant_id) REFERENCES product_variants(id) ON DELETE CASCADE;
ALTER TABLE variant_attribute_values ADD CONSTRAINT fk_vav_attrval FOREIGN KEY (attribute_value_id) REFERENCES attribute_values(id) ON DELETE CASCADE;

-- Đánh giá
ALTER TABLE feedbacks ADD CONSTRAINT fk_fb_user FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE feedbacks ADD CONSTRAINT fk_fb_prod FOREIGN KEY (product_id) REFERENCES products(id);

-- Giỏ hàng
ALTER TABLE carts ADD CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE cart_items ADD CONSTRAINT fk_ci_cart FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE;
ALTER TABLE cart_items ADD CONSTRAINT fk_ci_variant FOREIGN KEY (variant_id) REFERENCES product_variants(id);

-- Đơn hàng & Thanh toán & Vận chuyển
ALTER TABLE orders ADD CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE orders ADD CONSTRAINT fk_order_coupon FOREIGN KEY (coupon_id) REFERENCES coupons(id);
ALTER TABLE order_details ADD CONSTRAINT fk_od_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;
ALTER TABLE order_details ADD CONSTRAINT fk_od_variant FOREIGN KEY (variant_id) REFERENCES product_variants(id);

ALTER TABLE payments ADD CONSTRAINT fk_pay_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;
ALTER TABLE shipping_details ADD CONSTRAINT fk_sd_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE;