CREATE DATABASE IF NOT EXISTS banking_db;
USE banking_db;

DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS branches;
DROP TABLE IF EXISTS admins;

CREATE TABLE admins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL
);
INSERT INTO admins (username, password) VALUES ('admin', 'admin123');

CREATE TABLE branches (
    branch_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    manager_name VARCHAR(100) NOT NULL,
    contact VARCHAR(20) NOT NULL
);
INSERT INTO branches (branch_name, city, manager_name, contact) VALUES
('Main Branch', 'Karachi', 'Ali Khan', '021-1234567'),
('North Branch', 'Lahore', 'Sara Ahmed', '042-7654321');

CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    cnic VARCHAR(15) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    address TEXT,
    date_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE accounts (
    account_number VARCHAR(20) PRIMARY KEY,
    customer_id INT NOT NULL,
    account_type ENUM('Current','Saving') NOT NULL,
    balance DECIMAL(15,2) DEFAULT 0.00,
    branch_id INT NOT NULL,
    status ENUM('Active','Closed','Frozen') DEFAULT 'Active',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    FOREIGN KEY (branch_id) REFERENCES branches(branch_id)
);

CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    from_account VARCHAR(20),
    to_account VARCHAR(20),
    amount DECIMAL(15,2) NOT NULL,
    type ENUM('Deposit','Withdrawal','Transfer') NOT NULL,
    date_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
