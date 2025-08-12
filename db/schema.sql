-- users table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    verification_token VARCHAR(255) NOT NULL,
    verified BOOLEAN NOT NULL DEFAULT false,
);

-- insurance_products table
CREATE TABLE IF NOT EXISTS insurance_products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    is_bonus BOOLEAN NOT NULL,
    min_age INT NOT NULL,
    max_age INT NOT NULL,
    payment_term VARCHAR(50) NOT NULL,
    description TEXT
);



