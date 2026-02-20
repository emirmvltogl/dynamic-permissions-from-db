-- =========================
-- DATABASE
-- =========================
CREATE DATABASE IF NOT EXISTS dynamicauth;
USE dynamicauth;

-- =========================
-- USERS
-- =========================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150),
    password VARCHAR(255) NOT NULL
);

-- =========================
-- ROLES
-- =========================
CREATE TABLE roles (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- =========================
-- PERMISSIONS
-- =========================
CREATE TABLE permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    endpoint VARCHAR(255) NOT NULL,
    method VARCHAR(20) NOT NULL
);

-- =========================
-- USER_ROLES
-- =========================
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- =========================
-- ROLE_PERMISSIONS
-- =========================
CREATE TABLE role_permissions (
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
);

-- =========================
-- SEED ROLES
-- =========================
INSERT INTO roles (name) VALUES
('ADMIN'),
('USER');

-- =========================
-- SEED PERMISSIONS
-- =========================
INSERT INTO permissions (endpoint, method) VALUES
('/api/users', 'GET'),
('/api/users', 'POST'),
('/api/users/*', 'PUT'),
('/api/users/*', 'DELETE');

-- =========================
-- ROLE → PERMISSION LINK
-- =========================
-- ADMIN tüm izinlere sahip
INSERT INTO role_permissions (role_id, permission_id)
SELECT 1, id FROM permissions;

-- USER sadece GET izni
INSERT INTO role_permissions (role_id, permission_id)
VALUES (2, 1);

-- =========================
-- SAMPLE USER (password: 123456)
-- BCrypt hash
-- =========================
INSERT INTO users (username, email, password)
VALUES ('admin', 'admin@test.com',
'$2a$10$Dow1qL9LJp3o9v2s8eGf9uP2hTjGmH3h0S3H8YlV8lYQYkWjY5M4e');

-- USER → ROLE LINK
INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);
