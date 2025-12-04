-- Flyway baseline: create user table and insert two demo accounts
CREATE TABLE IF NOT EXISTS blog_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(100) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL
);

-- Admin and guest accounts (passwords are plain text for this learning skeleton)
INSERT INTO blog_user (username, password, role) VALUES
('admin', '123456', 'ADMIN'),
('guest', '123456', 'GUEST');
