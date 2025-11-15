-- Seed data for User Service
-- Version: 1.0
-- Description: Initial seed data for development and testing

-- Insert sample admin user (password: Admin@123)
INSERT INTO users (
    company_id, username, email, password_hash, first_name, last_name,
    status, type, email_verified, phone_verified
) VALUES (
    1,
    'admin',
    'admin@ecommerce.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYUv/rMYLMe', -- Admin@123
    'System',
    'Administrator',
    'ACTIVE',
    'ADMIN',
    TRUE,
    FALSE
) ON CONFLICT (email) DO NOTHING;

-- Assign admin roles
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_ADMIN' FROM users WHERE email = 'admin@ecommerce.com'
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_SUPER_ADMIN' FROM users WHERE email = 'admin@ecommerce.com'
ON CONFLICT DO NOTHING;

-- Insert sample customer user (password: Customer@123)
INSERT INTO users (
    company_id, username, email, password_hash, first_name, last_name,
    status, type, email_verified, phone_verified
) VALUES (
    1,
    'customer1',
    'customer@example.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYUv/rMYLMe', -- Customer@123
    'John',
    'Doe',
    'ACTIVE',
    'CUSTOMER',
    TRUE,
    FALSE
) ON CONFLICT (email) DO NOTHING;

-- Assign customer role
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_CUSTOMER' FROM users WHERE email = 'customer@example.com'
ON CONFLICT DO NOTHING;

-- Insert sample vendor user (password: Vendor@123)
INSERT INTO users (
    company_id, username, email, password_hash, first_name, last_name,
    status, type, email_verified, phone_verified
) VALUES (
    1,
    'vendor1',
    'vendor@example.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYUv/rMYLMe', -- Vendor@123
    'Jane',
    'Smith',
    'ACTIVE',
    'VENDOR',
    TRUE,
    FALSE
) ON CONFLICT (email) DO NOTHING;

-- Assign vendor role
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_VENDOR' FROM users WHERE email = 'vendor@example.com'
ON CONFLICT DO NOTHING;

-- Insert sample shopper user (password: Shopper@123)
INSERT INTO users (
    company_id, username, email, password_hash, first_name, last_name,
    status, type, email_verified, phone_verified
) VALUES (
    1,
    'shopper1',
    'shopper@example.com',
    '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewY5GyYUv/rMYLMe', -- Shopper@123
    'Mike',
    'Johnson',
    'ACTIVE',
    'SHOPPER',
    TRUE,
    FALSE
) ON CONFLICT (email) DO NOTHING;

-- Assign shopper role
INSERT INTO user_roles (user_id, role)
SELECT id, 'ROLE_SHOPPER' FROM users WHERE email = 'shopper@example.com'
ON CONFLICT DO NOTHING;
