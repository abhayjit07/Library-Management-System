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
