Banking Management System
A desktop-based banking management application built with Java Swing and MySQL. Developed as a Database course project. The system covers the core operations a bank admin would need: managing branches, customers, accounts, and transactions , all through a GUI interface.

Tech Stack

Java (Swing for GUI, JDBC for database connectivity)
MySQL
MySQL Connector/J 9.7.0


Features

Admin login with credential verification
Branch management (add, view, update branches)
Customer management (register customers with CNIC, contact, and address)
Account management (create Current/Saving accounts, freeze, close)
Transactions (deposit, withdrawal, transfer between accounts)
Reports panel for transaction history


Project Structure
banking-system/
├── src/

│   ├── BankingSystem.java          # Entry point
│   ├── dao/                        # Database access layer
│   ├── model/                      # Entity classes
│   ├── service/                    # Business logic
│   ├── util/                       # DB connection utility
│   └── view/                       # Swing GUI panels and frames

├── lib/
│   └── mysql-connector-j-9.7.0.jar
├── schema.sql
├── run.bat
└── run.sh

Database Design
Entity Relationship Diagram
Five entities: Admin, Branch, Customer, Account, and Transaction.

A Branch has many Accounts
A Customer has many Accounts
An Account initiates or receives Transactions
Admin is independent (handles system login)

Relational Schema
ADMIN        ( Id, Username, Password )

BRANCH       ( Branch_id, Branch_name, City, FirstName, LastName )
BRANCH_Contact ( Contact, Branch_id [FK] )

CUSTOMER     ( Customer_ID, FirstName, LastName, Cnic, Address, Email, Date_Created )

ACCOUNT      ( Account_number, Account_type, Balance, Status, Created_at,
               Customer_ID [FK], Branch_id [FK] )

TRANSACTION  ( Transaction_id, Amount, Type, Date_time,
               Account_number [FK] )

Setup and Run
Requirements: JDK 17 or above, MySQL running locally

Step 1 : Set up the database
sql-- Run schema.sql in MySQL Workbench or terminal
source schema.sql;
This creates the banking_db database with all tables and inserts default admin credentials.

Step 2 : Update DB credentials
Open src/util/DBConnection.java and update the username and password to match your MySQL setup.

Step 3 : Run
On Windows:
run.bat
On Linux/Mac:
bash run.sh
Or manually:
bashjavac -cp "lib/mysql-connector-j-9.7.0.jar" -d out $(find src -name "*.java")
java -cp "out;lib/mysql-connector-j-9.7.0.jar" BankingSystem
Default admin credentials:
Username: admin
Password: admin123

Author
Anas Khan
