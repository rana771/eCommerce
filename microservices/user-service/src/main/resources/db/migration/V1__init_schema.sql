-- User Service Database Schema
-- Version: 1.0
-- Description: Initial schema for user management

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create users table
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    phone_number VARCHAR(20),
    profile_image_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING_VERIFICATION',
    type VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    email_verified BOOLEAN DEFAULT FALSE,
    phone_verified BOOLEAN DEFAULT FALSE,
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    two_factor_secret VARCHAR(100),
    last_login TIMESTAMP,
    failed_login_attempts INTEGER DEFAULT 0,
    account_locked_until TIMESTAMP,
    password_reset_token VARCHAR(255),
    password_reset_expires TIMESTAMP,
    email_verification_token VARCHAR(255),
    email_verification_expires TIMESTAMP,
    refresh_token VARCHAR(500),
    refresh_token_expires TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP,
    CONSTRAINT chk_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING_VERIFICATION', 'DELETED')),
    CONSTRAINT chk_type CHECK (type IN ('CUSTOMER', 'VENDOR', 'SHOPPER', 'ADMIN', 'SUPPORT'))
);

-- Create user_roles table
CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT chk_role CHECK (role IN ('ROLE_CUSTOMER', 'ROLE_VENDOR', 'ROLE_SHOPPER', 'ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_SUPPORT', 'ROLE_MANAGER', 'ROLE_ANALYST'))
);

-- Create indexes for performance
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_company_id ON users(company_id);
CREATE INDEX idx_users_status ON users(status);
CREATE INDEX idx_users_type ON users(type);
CREATE INDEX idx_users_email_verified ON users(email_verified);
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_users_deleted_at ON users(deleted_at);
CREATE INDEX idx_users_password_reset_token ON users(password_reset_token);
CREATE INDEX idx_users_email_verification_token ON users(email_verification_token);
CREATE INDEX idx_users_refresh_token ON users(refresh_token);
CREATE INDEX idx_user_roles_role ON user_roles(role);

-- Create audit columns function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Create trigger to automatically update updated_at
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Create composite indexes for multi-tenancy queries
CREATE INDEX idx_users_company_status ON users(company_id, status);
CREATE INDEX idx_users_company_type ON users(company_id, type);
CREATE INDEX idx_users_company_email ON users(company_id, email);

-- Comments for documentation
COMMENT ON TABLE users IS 'Main user table storing all user types (customers, vendors, shoppers, admins)';
COMMENT ON TABLE user_roles IS 'User roles for authorization';
COMMENT ON COLUMN users.company_id IS 'Multi-tenancy: ID of the company this user belongs to';
COMMENT ON COLUMN users.status IS 'User account status';
COMMENT ON COLUMN users.type IS 'Type of user account';
COMMENT ON COLUMN users.two_factor_enabled IS 'Whether 2FA is enabled for this user';
COMMENT ON COLUMN users.failed_login_attempts IS 'Number of consecutive failed login attempts';
COMMENT ON COLUMN users.account_locked_until IS 'Timestamp until which account is locked';
COMMENT ON COLUMN users.refresh_token IS 'JWT refresh token for token refresh flow';
COMMENT ON COLUMN users.deleted_at IS 'Soft delete timestamp';
