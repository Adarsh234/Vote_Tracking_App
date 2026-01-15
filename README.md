# Vote Tracking Application 🗳️

A secure and modern desktop Voting System built with **Java Swing**. This project implements a robust **MVC (Model-View-Controller)** architecture and uses the **DAO (Data Access Object)** design pattern to ensure clean separation between the user interface and database operations.

## 🚀 Key Features

* **Secure Authentication:** Admin login system connected to a MySQL database to prevent unauthorized access.
* **Modern UI:** A clean, flat-design interface featuring a professional dark theme (`#2C3E50`) with orange accents (`#E67E22`).
* **Real-time Dashboard:** Interactive dashboard to cast votes and view live election standings instantly.
* **Persistent Data:** All votes and user credentials are stored permanently in a MySQL database.
* **Loading Screen:** A dynamic splash screen with a simulated progress bar for a polished user experience.

## 🛠️ Tech Stack

* **Language:** Java (JDK 8+)
* **GUI Framework:** Java Swing & AWT
* **Database:** MySQL
* **Design Patterns:** * **DAO** (Data Access Object) for database logic.
    * **DTO** (Data Transfer Object) for data transport.
    * **Singleton** for efficient Database Connections.

## 📂 Project Architecture

The project is organized into logical packages to ensure maintainability:

```text
src/com/adarsh/
├── dao/          # Data Access Objects (Database Logic)
│   ├── DB.java        -> Singleton Database Connection
│   ├── UserDao.java   -> Handles User Authentication queries
│   └── VoteDao.java   -> Handles Voting & Result queries
├── dto/          # Data Transfer Objects
│   └── UserDto.java   -> Carries user data between layers
├── ui/           # User Interface (Swing Views)
│   ├── SplaceScreen.java -> Loading Window with Progress Bar
│   ├── Login.java        -> Authentication Screen
│   └── Dashboard.java    -> Main Voting & Results Interface

```

## ⚙️ Setup & Installation

### 1. Prerequisites

* Java Development Kit (JDK) installed.
* MySQL Server installed and running.
* `mysql-connector-j` (JDBC Driver) added to your project's libraries.

### 2. Database Configuration

Run the following SQL commands in your MySQL Workbench or Command Line to set up the necessary tables:

```sql
CREATE DATABASE bookingtb;
USE bookingtb;

-- Create Users Table for Authentication
CREATE TABLE users (
    userid VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50)
);

-- Create Candidates Table for Voting
CREATE TABLE candidates (
    name VARCHAR(50) PRIMARY KEY,
    votes INT DEFAULT 0
);

-- Insert Default Data
INSERT INTO users (userid, password) VALUES ('admin', '123');
INSERT INTO candidates (name, votes) VALUES ('Party A', 0), ('Party B', 0), ('Party C', 0);

```

### 3. Update Connection Details

Navigate to `src/com/adarsh/dao/DB.java` and ensure your database credentials match your local setup:

```java
String url = "jdbc:mysql://localhost:3306/bookingtb";
String user = "root";       // Your MySQL Username
String pass = "your_password"; // Your MySQL Password

```

### 4. Run the Application

Start the application by running the Splash Screen file:
`src/com/adarsh/ui/SplaceScreen.java`

## 📸 Screenshots
<img width="1108" height="867" alt="image" src="https://github.com/user-attachments/assets/011ce7cf-6940-45d1-9692-ee0b740ad427" />


<img width="1108" height="867" alt="image" src="https://github.com/user-attachments/assets/e3d467b7-5b1e-44c0-808d-a6c6d27448f0" />


## 🤝 Contributing

Contributions, issues, and feature requests are welcome!

## 📜 License

This project is open-source.

```
