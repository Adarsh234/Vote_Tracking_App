# Vote Tracking Application 🗳️

A secure, enterprise-grade Voting Management System built with **Java Swing** and **MySQL**. This project features a robust **Role-Based Access Control (RBAC)** system, allowing Administrators to manage elections dynamically and Users to vote securely in real-time.

## 🚀 Key Features

### 🔐 Security & Architecture
* **MVC & DAO Patterns:** Clean separation of concerns (Model-View-Controller & Data Access Object).
* **Role-Based Login:** Smart authentication that directs users to specific dashboards based on their role (`Admin` vs `User`).
* **Double-Vote Prevention:** Database flags ensure users can only vote **once** per election.

### 👨‍💼 Administrator Dashboard
* **Dynamic Candidate Management:** Admin can start a new election by typing custom candidate names (e.g., "Alice, Bob, Charlie"). The system automatically generates the ballot.
* **Poll Configuration:** Update the active poll question instantly.
* **Danger Zone:** "Reset Election" functionality to wipe all votes and reset user statuses for a fresh poll.

### 👤 User Dashboard
* **Dynamic Voting Interface:** Radio buttons are automatically generated based on the current candidates in the database.
* **Live Split-Screen:** Users see the voting form on the left and a **Real-Time Result Matrix** on the right.
* **One-Vote Policy:** Once a vote is cast, the interface locks to prevent duplicate submissions.

## 🛠️ Tech Stack

* **Language:** Java (JDK 8+)
* **GUI Framework:** Java Swing & AWT (Flat Design/Dark Theme)
* **Database:** MySQL
* **Connectivity:** JDBC (Java Database Connectivity)

## 📂 Project Structure

```text
src/com/adarsh/
├── dao/              # Database Logic
│   ├── DB.java       -> Singleton Database Connection
│   ├── UserDao.java  -> Auth & Role Verification
│   └── VoteDao.java  -> Dynamic Candidate & Voting Logic
├── ui/               # User Interface
│   ├── SplaceScreen.java -> Loading Animation
│   ├── Login.java        -> Role-Based Login Screen
│   ├── AdminDashboard.java -> Election Management Console
│   └── UserDashboard.java  -> Voting & Results Panel

```

## ⚙️ Setup & Installation

### 1. Database Setup (Crucial)

You must update your MySQL database to support roles and dynamic polling. Run this script in **MySQL Workbench**:

```sql
CREATE DATABASE IF NOT EXISTS bookingtb;
USE bookingtb;

-- 1. Users Table (With Role & Vote Status)
CREATE TABLE users (
    userid VARCHAR(50) PRIMARY KEY,
    password VARCHAR(50),
    role VARCHAR(20) DEFAULT 'user',  -- 'admin' or 'user'
    has_voted TINYINT DEFAULT 0       -- 0 = False, 1 = True
);

-- 2. Candidates Table (Stores Dynamic Nominees)
CREATE TABLE candidates (
    name VARCHAR(50) PRIMARY KEY,
    votes INT DEFAULT 0
);

-- 3. Poll Settings (Stores the Question)
CREATE TABLE poll_settings (
    id INT PRIMARY KEY,
    question VARCHAR(255)
);

-- 4. Insert Default Data
INSERT INTO users (userid, password, role) VALUES 
('admin', '123', 'admin'),
('user1', '123', 'user'),
('user2', '123', 'user');

INSERT INTO poll_settings VALUES (1, 'Who is your favorite superhero?');
INSERT INTO candidates (name) VALUES ('Iron Man'), ('Captain America'), ('Thor');

```

### 2. Configure Java Connection

Open `src/com/adarsh/dao/DB.java` and update your MySQL credentials:

```java
String url = "jdbc:mysql://localhost:3306/bookingtb";
String user = "root";       // Your MySQL Username
String pass = "your_password"; // Your MySQL Password

```

### 3. Run the App

Run the **Splash Screen** to start the application:
`src/com/adarsh/ui/SplaceScreen.java`

## 📖 Usage Guide

| Role | Username | Password | Capabilities |
| --- | --- | --- | --- |
| **Admin** | `admin` | `123` | Create Polls, Add Candidates, Reset System |
| **User** | `user1` | `123` | Vote (One-time), View Live Results |

## 📸 Screenshots

**Login Screen**
<img width="1108" height="867" alt="image" src="https://github.com/user-attachments/assets/3cb147da-6e89-4d55-9889-c818ce841956" />

**Admin Dashbard**
<img width="1108" height="930" alt="image" src="https://github.com/user-attachments/assets/b03c3a4c-5e3e-4a65-bc34-28a93d6068c0" />

**User Dashboard**
<img width="1108" height="803" alt="image" src="https://github.com/user-attachments/assets/beacc4df-c8a4-44be-854e-61b09165a629" />

## 🤝 Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## 📜 License

This project is open-source.

```

```
