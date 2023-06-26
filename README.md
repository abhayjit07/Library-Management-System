# Library-Management-System
## Introduction
The Library Management System is a Java Swing-based application that allows users to manage a library database efficiently. It provides both user login and admin login functionalities. The graphical user interface (GUI) is developed using Java Swing, which offers a user-friendly experience and is powered by a MySQL database.

## Features

### Admin Functionality:
1) **Admin Book Upload:**
   - This feature allows the admin to add new books to the library database.
   - The admin can enter details such as the title, author, publication, and quantity of the book.
   - The information is stored in the database, creating a new entry for the book.

2) **Admin Book Management:**
   - The admin has access to all existing books in the library.
   - They can view the titles, authors, and available quantities of each book.
   - The admin can also modify the number of books when necessary.

3) **Admin User Management:**
   - The admin can view and manage all existing users in the system.
   - They can add new users by entering their details, such as username, name, roll number, and program.

4) **Admin Book History:**
   - The admin can retrieve the lending history of a particular book.
   - This feature provides information about previous borrowing instances, including student names and details.

5) **Admin Pending Return Books:**
   - The admin can check the list of books that are pending return from students.
   - It provides information about the lending date, expected return date, and any applicable penalties.
   - This feature helps the admin monitor book returns and take appropriate actions.

### User Functionality:
1) **User Login:**
   - Registered users can log in to their personalized accounts within the Library Management System.
   - Authentication is required using a username and password.

2) **User Book Viewing:**
   - Users can view all available books in the library.
   - They can also search for specific books based on titles or other relevant details.

3) **User Book Lending:**
   - Users can borrow books from the library.
   - They can search for a book and check its availability before proceeding with the lending process.
   - The system records the lending details, including the book title, student information, and lending date.

4) **User Book Return:**
   - Users can return books they have borrowed within the specified due date.
   - The system records the return date, updates the book quantity, and calculates fines for overdue books.

5) **User Fine Management:**
   - Users can check the total fine amount they owe for overdue books.
   - The system provides transparency and allows users to make fine payments.
   - It calculates the change to be returned or updates the remaining fine accordingly.

### General Functionality:
1) **Admin-User View Separation:**
   - Provides a clear distinction between admin and regular user interfaces.
   - Admins can access advanced functionalities without cluttering the user interface.
   - Enhances security by restricting access to administrative features to authorized personnel.

A Java Swing-based library management system with a MySQL database.

## Requirements

To execute the project, you will need the following software installed:

- MySQL Community Server: The project utilizes MySQL as the database management system. You can download and install MySQL Community Server from the official website: [MySQL Downloads](https://dev.mysql.com/downloads/)

- MySQL JDBC Connector: The JDBC connector is required to establish a connection between the Java application and the MySQL database. You can download the latest version of the MySQL JDBC Connector JAR file from the official website: [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/)

- JDK (Java Development Kit): The project is developed using Java, so you need to have JDK installed on your system. You can download and install the latest version of JDK from the Oracle website: [Java SE Downloads](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)

Make sure to install and configure the above software properly before executing the project.


## Usage

1. Launch the application.

2. Enter your MySQL username and password.
 ![image](https://github.com/abhayjit07/Library-Management-System/assets/100589347/c07877dc-801b-40e4-9a5b-fc39fe3d30b1)


3. Login as an admin or a user using the provided credentials.

   - To access the admin login:
     - Username: admin
     - Password: admin123

   - For user logins, please refer to the account credentials provided by the admin.

4. Explore the available functionalities based on your role.


## Database Configuration

To set up the necessary tables in MySQL for the library management system, follow these steps:

1. Create a database called `lib`:
CREATE DATABASE lib;
2. Use the `lib` database:
USE lib

3. Execute the following series of SQL statements to create the required tables:

```sql
-- "Books" table stores information about the available books in the library.
CREATE TABLE Books (
    BookId INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    Author VARCHAR(255) NOT NULL,
    ISBN VARCHAR(255) NOT NULL,
    Quantity INT NOT NULL
);

-- "History" table maintains a record of the book lending history.
CREATE TABLE History (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    BookTitle VARCHAR(255) NOT NULL,
    StudentName VARCHAR(255) NOT NULL,
    LendingDate DATE NOT NULL,
    ReturnDate DATE,
    RollNumber INT NOT NULL,
    Programme VARCHAR(255) NOT NULL,
    Year VARCHAR(255) NOT NULL
);

-- "Students" table stores information about the students.
CREATE TABLE Students (
    Student VARCHAR(200),
    Roll_no INT PRIMARY KEY,
    Programme VARCHAR(200),
    Year INT,
    username VARCHAR(255),
    password VARCHAR(255),
    Fine INT
);

-- "Lending" table tracks the lending activity of books.
CREATE TABLE Lending (
    LendingId INT,
    BookTitle VARCHAR(255),
    LendingDate DATE,
    ReturnDate DATE,
    RollNumber INT,
    FOREIGN KEY (RollNumber) REFERENCES Students(Roll_no)
);
```
By setting up these tables, the library management system can efficiently store and retrieve data related to books, students, lending history, and book availability. The tables establish the necessary relationships and enable seamless management of the library's resources.

## Conclusion

The Library Management System is a comprehensive solution for managing books, users, and lending activities in a library setting. With its user-friendly interface and robust functionality, it simplifies the process of book management, lending, and return tracking.

We welcome feedback, bug reports, and contributions from the community to further enhance the system's capabilities. Together, we can continue improving the Library Management System and making it even more valuable for libraries and their patrons.

Thank you for using the Library Management System. If you have any questions or need assistance, please don't hesitate to reach out.

