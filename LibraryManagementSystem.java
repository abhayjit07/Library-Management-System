import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.table.DefaultTableModel;

public class LibraryManagementSystem extends JFrame {
    private int rollNumber = -1;
    private static final String DB_URL = "jdbc:mysql://localhost/lib"; // enter your database name here
    private static final String DB_USERNAME = "root"; // enter your username here
    private static final String DB_PASSWORD = "asdfghj"; // enter your password here

    private JButton adminButton;
    private JButton userButton;
    private JButton exitButton;

    public LibraryManagementSystem() {
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initializeComponents();
        addComponentsToFrame();
        attachListeners();

        pack();
        setLocationRelativeTo(null); // Center the frame on the screen
    }

    private void initializeComponents() {
        adminButton = new JButton("Admin");
        userButton = new JButton("User");
        exitButton = new JButton("Exit");
    }

    private void addComponentsToFrame() {
        setLayout(new BorderLayout());

        // Create and configure the welcome label
        JLabel welcomeLabel = new JLabel("Welcome to the Library Management System");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Add some padding

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout()); // Use GridBagLayout for more flexibility

        // Configure GridBagConstraints for button positioning and spacing
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10); // Adjust the spacing between buttons

        // Add the buttons to the button panel
        gbc.gridx = -1;
        gbc.gridy = 0;
        buttonPanel.add(adminButton, gbc);

        gbc.gridx = 1;
        buttonPanel.add(userButton, gbc);

        gbc.gridx = 2;
        buttonPanel.add(exitButton, gbc);

        // Add the welcome label and button panel to the frame
        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Set the default frame size
        int frameWidth = 600;
        int frameHeight = 300;
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        pack(); // Resize the frame to fit the components
    }

    private void attachListeners() {
        adminButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openAdminView();
            }
        });

        userButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openUserView();
            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }

    private void openAdminView() {
        String username = JOptionPane.showInputDialog("Enter admin username:");
        String password = JOptionPane.showInputDialog("Enter admin password:");

        // Check if the provided username and password are valid
        if (isValidAdmin(username, password)) {
            AdminView adminView = new AdminView();
            adminView.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid admin credentials. Please try again.");
        }
    }

    private boolean isValidAdmin(String username, String password) {
        // Check if the username and password match the admin credentials
        return username.equals("admin") && password.equals("admin123");
    }

    private void openUserView() {
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");

        if (isValidUser(username, password)) {
            UserView userView = new UserView();
            userView.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
        }
    }

    private boolean isValidUser(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
            String sql = "SELECT password, Roll_no FROM students WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                String storedPassword = result.getString("password");
                int rollNo = result.getInt("Roll_no");
                if (storedPassword.equals(password)) {
                    rollNumber = rollNo; // Update rollNumber field
                    // System.out.println(rollNumber);
                    return true;
                }
            }
            return false;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to login the user. Error: " + ex.getMessage());
            ex.printStackTrace(); // Print the error stack trace for debugging
            return false;
        }
    }

    private class AdminView extends JFrame {
        private JButton bookUploadButton;
        private JButton bookHistoryButton;
        private JButton pendingReturnsButton;
        private JButton bookUpdateButton;
        private JButton addUserButton;
        private JButton displayUsersButton;
        private JButton displayBooksButton;
        private JButton backButton;

        public AdminView() {
            setTitle("Admin View");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            initializeComponents();
            addComponentsToFrame();
            attachListeners();

            pack();
            setLocationRelativeTo(null); // Center the frame on the screen
        }

        private void initializeComponents() {
            bookUploadButton = new JButton("Book Upload");
            bookHistoryButton = new JButton("Book History");
            pendingReturnsButton = new JButton("Pending Returns");
            bookUpdateButton = new JButton("Book Update");
            addUserButton = new JButton("Add User");
            displayUsersButton = new JButton("Display Users");
            displayBooksButton = new JButton("Display Books");
            backButton = new JButton("Back");
        }

        private void addComponentsToFrame() {
            setLayout(new GridLayout(4, 1));

            add(bookUploadButton);
            add(bookHistoryButton);
            add(pendingReturnsButton);
            add(bookUpdateButton);
            add(addUserButton);
            add(displayUsersButton);
            add(displayBooksButton);
            add(backButton);

            int frameWidth = 600;
            int frameHeight = 300;
            setPreferredSize(new Dimension(frameWidth, frameHeight));
        }

        private void attachListeners() {
            bookUploadButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adminBookUpload();
                }
            });

            bookHistoryButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adminBookHistory();
                }
            });

            pendingReturnsButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adminPendingReturns();
                }
            });

            bookUpdateButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adminBookUpdate();
                }
            });

            addUserButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adminAddUser();
                }
            });

            displayUsersButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adminDisplayUsers();
                }
            });

            displayBooksButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    adminDisplayBooks();
                }
            });

            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close the admin view
                }
            });
        }

        private void adminBookUpload() {
            String title = JOptionPane.showInputDialog("Enter Book Title:");

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                // Check if the book already exists in the database
                String checkBookSql = "SELECT * FROM books WHERE title = ?";
                try (PreparedStatement checkStatement = connection.prepareStatement(checkBookSql)) {
                    checkStatement.setString(1, title);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (resultSet.next()) {
                            int existingQuantity = resultSet.getInt("quantity");
                            String quantityStr = JOptionPane
                                    .showInputDialog(
                                            "Enter Quantity to be added (Existing: " + existingQuantity + "):");
                            int quantity = Integer.parseInt(quantityStr);

                            if (quantity <= 0) {
                                JOptionPane.showMessageDialog(null,
                                        "Invalid quantity. Please enter a positive number.");
                                return;
                            }

                            // Update the quantity of the existing book
                            String updateQuantitySql = "UPDATE books SET quantity = ? WHERE title = ?";
                            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuantitySql)) {
                                updateStatement.setInt(1, existingQuantity + quantity);
                                updateStatement.setString(2, title);
                                updateStatement.executeUpdate();
                            }

                            JOptionPane.showMessageDialog(null, "Book quantity updated successfully!");
                            return;
                        }
                    }
                }

                // If the book does not already exist, ask for author, isbn, and quantity
                String author = JOptionPane.showInputDialog("Enter Author Name:");
                String isbn = JOptionPane.showInputDialog("Enter ISBN:");
                String quantityStr = JOptionPane.showInputDialog("Enter Available Quantity:");

                int quantity = Integer.parseInt(quantityStr);

                if (quantity <= 0) {
                    JOptionPane.showMessageDialog(null, "Invalid quantity. Please enter a positive number.");
                    return;
                }

                String insertBookSql = "INSERT INTO books (title, author, isbn, quantity) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(insertBookSql)) {
                    statement.setString(1, title);
                    statement.setString(2, author);
                    statement.setString(3, isbn);
                    statement.setInt(4, quantity);
                    statement.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Book uploaded successfully!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Failed to upload book. Error: " + ex.getMessage());
            }
        }

        private void adminBookUpdate() {
            String title = JOptionPane.showInputDialog("Enter Book Title:");
            if (title == null || title.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Book title cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String quantityStr = JOptionPane.showInputDialog("Enter New Available Quantity:");
            int quantity = Integer.parseInt(quantityStr);

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String sql = "UPDATE books SET quantity = ? WHERE title = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, quantity);
                statement.setString(2, title);
                int rowsUpdated = statement.executeUpdate();

                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(null, "Book Quantity Updated Successfully!");
                } else {
                    JOptionPane.showMessageDialog(null, "Book not found in the system.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Failed to update book. Error: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        private void adminBookHistory() {
            String bookTitle = JOptionPane.showInputDialog(null, "Enter Book Title:", "Admin Book History",
                    JOptionPane.PLAIN_MESSAGE);
            if (bookTitle != null) {
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                    String sql = "SELECT * FROM History WHERE BookTitle = ? ORDER BY LendingDate DESC";
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, bookTitle);
                        try (ResultSet resultSet = statement.executeQuery()) {
                            StringBuilder history = new StringBuilder("\nBook History:\n-----------------\n");
                            while (resultSet.next()) {
                                String studentName = resultSet.getString("StudentName");
                                String rollNumber = resultSet.getString("RollNumber");
                                String programme = resultSet.getString("Programme");
                                String year = resultSet.getString("Year");
                                LocalDate lendingDate = resultSet.getDate("LendingDate").toLocalDate();
                                LocalDate returnDate = resultSet.getDate("ReturnDate") != null
                                        ? resultSet.getDate("ReturnDate").toLocalDate()
                                        : null;

                                history.append("Student Name: ").append(studentName).append("\n")
                                        .append("Roll Number: ").append(rollNumber).append("\n")
                                        .append("Programme: ").append(programme).append("\n")
                                        .append("Year: ").append(year).append("\n")
                                        .append("Lending Date: ").append(lendingDate).append("\n")
                                        .append("Return Date: ")
                                        .append(returnDate != null ? returnDate : "Not returned yet").append("\n")
                                        .append("-----------------\n");
                            }

                            JOptionPane.showMessageDialog(null, history.toString(), "Book History",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error occurred while accessing the database.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }

        private void adminPendingReturns() {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String sql = "SELECT * FROM History WHERE ReturnDate IS NULL";
                try (Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(sql)) {
                    StringBuilder pendingReturns = new StringBuilder("Pending Return Books:\n---------------------\n");
                    while (resultSet.next()) {
                        String bookTitle = resultSet.getString("BookTitle");
                        String studentName = resultSet.getString("StudentName");
                        String rollNumber = resultSet.getString("RollNumber");
                        String programme = resultSet.getString("Programme");
                        String year = resultSet.getString("Year");
                        LocalDate lendingDate = resultSet.getDate("LendingDate").toLocalDate();
                        LocalDate expectedReturnDate = lendingDate.plusDays(14);
                        LocalDate currentDate = LocalDate.now();

                        long daysOverdue = currentDate.toEpochDay() - expectedReturnDate.toEpochDay();
                        double penalty = daysOverdue > 0 ? daysOverdue * 10 : 0;

                        pendingReturns.append("Book Title: ").append(bookTitle).append("\n")
                                .append("Student Name: ").append(studentName).append("\n")
                                .append("Roll Number: ").append(rollNumber).append("\n")
                                .append("Programme: ").append(programme).append("\n")
                                .append("Year: ").append(year).append("\n")
                                .append("Lending Date: ").append(lendingDate).append("\n")
                                .append("Expected Return Date: ").append(expectedReturnDate).append("\n")
                                .append("Penalty: Rs. ").append(penalty).append("\n\n");
                    }
                    JOptionPane.showMessageDialog(null, pendingReturns.toString());
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error occurred while accessing the database.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        private void adminDisplayUsers() {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String sql = "SELECT * FROM STUDENTS";
                try (Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(sql)) {
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("Name");
                    model.addColumn("Roll Number");
                    model.addColumn("Programme");
                    model.addColumn("Year");
                    model.addColumn("Username");
                    model.addColumn("Password");
                    model.addColumn("Fine");

                    while (resultSet.next()) {
                        String name = resultSet.getString("Student");
                        Integer rollNum = resultSet.getInt("Roll_no");
                        String programme = resultSet.getString("Programme");
                        String year = resultSet.getString("Year");
                        String username = resultSet.getString("username");
                        String password = resultSet.getString("password");
                        String fine = resultSet.getString("Fine");

                        model.addRow(new Object[] { name, rollNum, programme, year, username, password, fine });
                    }

                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setPreferredSize(new Dimension(800, 300));

                    JOptionPane.showMessageDialog(null, scrollPane, "Users", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error occurred while accessing the database.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        private void adminAddUser() {
            String name = JOptionPane.showInputDialog("Enter Name:");
            Integer rollNum = Integer.parseInt(JOptionPane.showInputDialog("Enter Roll Number(int):"));
            String programme = JOptionPane.showInputDialog("Enter Programme:");
            String year = JOptionPane.showInputDialog("Enter Year:");
            String username = JOptionPane.showInputDialog("Enter Username:");
            String password = JOptionPane.showInputDialog("Enter Password:");

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                // Check if the roll number, username, or password already exist
                String checkExistingSql = "SELECT * FROM STUDENTS WHERE Roll_no = ? OR username = ?";
                try (PreparedStatement checkStatement = connection.prepareStatement(checkExistingSql)) {
                    checkStatement.setInt(1, rollNum);
                    checkStatement.setString(2, username);
                    try (ResultSet resultSet = checkStatement.executeQuery()) {
                        if (resultSet.next()) {
                            JOptionPane.showMessageDialog(null,
                                    "User with the same Roll Number or Username already exists!");
                            return; // Exit the method if a duplicate is found
                        }
                    }
                }
                // Insert the new user
                String insertSql = "INSERT INTO STUDENTS VALUES (?, ?, ?, ?, ?, ?, 0)";
                try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                    statement.setString(1, name);
                    statement.setInt(2, rollNum);
                    statement.setString(3, programme);
                    statement.setString(4, year);
                    statement.setString(5, username);
                    statement.setString(6, password); // Store the password as plain text

                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "User added successfully!");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Failed to add user. Error: " + ex.getMessage());
            }
        }

        private void adminDisplayBooks() {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String sql = "SELECT * FROM Books";
                try (Statement statement = connection.createStatement();
                        ResultSet resultSet = statement.executeQuery(sql)) {
                    DefaultTableModel model = new DefaultTableModel();
                    model.addColumn("Title");
                    model.addColumn("Author");
                    model.addColumn("Quantity");

                    while (resultSet.next()) {
                        String title = resultSet.getString("Title");
                        String author = resultSet.getString("Author");
                        int quantity = resultSet.getInt("Quantity");

                        model.addRow(new Object[] { title, author, quantity });
                    }

                    JTable table = new JTable(model);
                    JScrollPane scrollPane = new JScrollPane(table);
                    scrollPane.setPreferredSize(new Dimension(500, 300));

                    JOptionPane.showMessageDialog(null, scrollPane, "Available Books", JOptionPane.PLAIN_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        null,
                        "Error occurred while accessing the database.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

    }

    private class UserView extends JFrame {
        private JButton bookViewButton;
        private JButton bookLendingButton;
        private JButton bookReturnButton;
        private JButton payFineButton;
        private JButton viewIssuedButton;
        private JButton backButton;

        public UserView() {
            setTitle("User View");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            initializeComponents();
            addComponentsToFrame();
            attachListeners();

            pack();
            setLocationRelativeTo(null); // Center the frame on the screen
        }

        private void initializeComponents() {
            bookViewButton = new JButton("Book View");
            bookLendingButton = new JButton("Book Lending");
            bookReturnButton = new JButton("Book Return");
            payFineButton = new JButton("Check and Pay Fine");
            viewIssuedButton = new JButton("View Issued Books");
            backButton = new JButton("Back");
        }

        private void addComponentsToFrame() {
            setLayout(new GridLayout(5, 1));

            add(bookViewButton);
            add(bookLendingButton);
            add(bookReturnButton);
            add(payFineButton);
            add(viewIssuedButton);
            add(backButton);
            int frameWidth = 600;
            int frameHeight = 300;
            setPreferredSize(new Dimension(frameWidth, frameHeight));
        }

        private void attachListeners() {
            bookViewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    userBookView();
                }
            });

            bookLendingButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    userBookLending();
                }
            });

            bookReturnButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    userBookReturn();
                }
            });
            payFineButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    payFine();
                }
            });

            viewIssuedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showCurrentBooks();
                }
            });

            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose(); // Close the user view
                }
            });
        }

        private void userBookView() {
            String[] options = { "View All Books", "Search Books by Title" };
            int choice = JOptionPane.showOptionDialog(
                    null,
                    "Student Book View",
                    "Library Management System",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (choice == 0) {
                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                    String sql = "SELECT * FROM Books";
                    try (Statement statement = connection.createStatement();
                            ResultSet resultSet = statement.executeQuery(sql)) {
                        DefaultTableModel model = new DefaultTableModel();
                        model.addColumn("Title");
                        model.addColumn("Author");
                        model.addColumn("Quantity");

                        while (resultSet.next()) {
                            String title = resultSet.getString("Title");
                            String author = resultSet.getString("Author");
                            int quantity = resultSet.getInt("Quantity");

                            model.addRow(new Object[] { title, author, quantity });
                        }

                        JTable table = new JTable(model);
                        JScrollPane scrollPane = new JScrollPane(table);
                        scrollPane.setPreferredSize(new Dimension(500, 300));

                        JOptionPane.showMessageDialog(null, scrollPane, "Available Books", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Error occurred while accessing the database.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else if (choice == 1) {
                String searchTitle = JOptionPane.showInputDialog(null, "Enter Book Title:", "Search Books by Title",
                        JOptionPane.PLAIN_MESSAGE);
                if (searchTitle != null) {
                    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                        String sql = "SELECT * FROM Books WHERE Title LIKE ?";
                        try (PreparedStatement statement = connection.prepareStatement(sql)) {
                            statement.setString(1, "%" + searchTitle + "%");
                            try (ResultSet resultSet = statement.executeQuery()) {
                                DefaultTableModel model = new DefaultTableModel();
                                model.addColumn("Title");
                                model.addColumn("Author");
                                model.addColumn("Quantity");

                                while (resultSet.next()) {
                                    String title = resultSet.getString("Title");
                                    String author = resultSet.getString("Author");
                                    int quantity = resultSet.getInt("Quantity");

                                    model.addRow(new Object[] { title, author, quantity });
                                }

                                JTable table = new JTable(model);
                                JScrollPane scrollPane = new JScrollPane(table);
                                scrollPane.setPreferredSize(new Dimension(500, 300));

                                JOptionPane.showMessageDialog(null, scrollPane, "Search Results",
                                        JOptionPane.PLAIN_MESSAGE);
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Error occurred while accessing the database.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        }

        private void userBookLending() {
            String bookTitle = JOptionPane.showInputDialog(null, "Enter Book Title:", "Book Lending",
                    JOptionPane.PLAIN_MESSAGE);
            if (bookTitle != null) {
                String lendingDateStr = JOptionPane.showInputDialog(null, "Enter Lending Date (YYYY-MM-DD):",
                        "Book Lending",
                        JOptionPane.PLAIN_MESSAGE);
                if (lendingDateStr != null) {
                    LocalDate lendingDate = LocalDate.parse(lendingDateStr);

                    try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                        String checkBookSql = "SELECT * FROM Books WHERE Title = ?";
                        try (PreparedStatement statement = connection.prepareStatement(checkBookSql)) {
                            statement.setString(1, bookTitle);
                            try (ResultSet resultSet = statement.executeQuery()) {
                                if (resultSet.next()) {
                                    int quantity = resultSet.getInt("Quantity");
                                    if (quantity > 0) {
                                        String insertLendingSql = "INSERT INTO Lending (RollNumber, BookTitle, LendingDate) VALUES (?, ?, ?)";
                                        try (PreparedStatement lendingStatement = connection
                                                .prepareStatement(insertLendingSql)) {
                                            lendingStatement.setInt(1, rollNumber);
                                            lendingStatement.setString(2, bookTitle);
                                            lendingStatement.setDate(3, java.sql.Date.valueOf(lendingDate));
                                            lendingStatement.executeUpdate();

                                            String updateQuantitySql = "UPDATE Books SET Quantity = Quantity - 1 WHERE Title = ?";
                                            try (PreparedStatement updateStatement = connection
                                                    .prepareStatement(updateQuantitySql)) {
                                                updateStatement.setString(1, bookTitle);
                                                updateStatement.executeUpdate();
                                            }

                                            String sql = "SELECT Student, Programme, Year FROM students WHERE Roll_no = ?";
                                            try (PreparedStatement statement_2 = connection.prepareStatement(sql)) {
                                                statement_2.setInt(1, rollNumber);
                                                ResultSet result_2 = statement_2.executeQuery();
                                                if (result_2.next()) {
                                                    String storedName = result_2.getString("Student");
                                                    String storedProgramme = result_2.getString("Programme");
                                                    String storedYear = result_2.getString("Year");

                                                    String insertHistorySql = "INSERT INTO History (BookTitle, StudentName, RollNumber, Programme, Year, LendingDate) VALUES (?, ?, ?, ?, ?, ?)";
                                                    try (PreparedStatement historyStatement = connection
                                                            .prepareStatement(insertHistorySql)) {
                                                        historyStatement.setString(1, bookTitle);
                                                        historyStatement.setString(2, storedName);
                                                        historyStatement.setInt(3, rollNumber);
                                                        historyStatement.setString(4, storedProgramme);
                                                        historyStatement.setString(5, storedYear);
                                                        historyStatement.setDate(6, java.sql.Date.valueOf(lendingDate));
                                                        historyStatement.executeUpdate();
                                                    }

                                                    JOptionPane.showMessageDialog(
                                                            null,
                                                            "Book lent successfully.",
                                                            "Success",
                                                            JOptionPane.INFORMATION_MESSAGE);
                                                }
                                            }
                                        }
                                    } else {
                                        JOptionPane.showMessageDialog(
                                                null,
                                                "Book is not available for lending.",
                                                "Unavailable",
                                                JOptionPane.WARNING_MESSAGE);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(
                                            null,
                                            "Book not found.",
                                            "Not Found",
                                            JOptionPane.WARNING_MESSAGE);
                                }
                            }
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Error occurred while accessing the database.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }
                }
            }
        }

        private void userBookReturn() {
            String bookTitle = JOptionPane.showInputDialog(null, "Enter Book Title:", "Book Return",
                    JOptionPane.PLAIN_MESSAGE);

            if (bookTitle != null) {
                LocalDate returnDate = LocalDate.now(); // Set the return date as the current date

                try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                    // Check if the book is already returned
                    String checkReturnStatusSql = "SELECT ReturnDate FROM History WHERE BookTitle = ? AND RollNumber = ? AND ReturnDate IS NULL";
                    try (PreparedStatement checkStatement = connection.prepareStatement(checkReturnStatusSql)) {
                        checkStatement.setString(1, bookTitle);
                        checkStatement.setInt(2, rollNumber);
                        try (ResultSet resultSet = checkStatement.executeQuery()) {
                            if (!resultSet.next()) {
                                JOptionPane.showMessageDialog(
                                        null,
                                        "This book is not borrowed or has already been returned.",
                                        "Not Borrowed or Already Returned",
                                        JOptionPane.WARNING_MESSAGE);
                                return;
                            }
                        }
                    }

                    // Update return date
                    String updateReturnDateSql = "UPDATE History SET ReturnDate = ? WHERE BookTitle = ? AND RollNumber = ? AND ReturnDate IS NULL";
                    try (PreparedStatement statement = connection.prepareStatement(updateReturnDateSql)) {
                        statement.setDate(1, java.sql.Date.valueOf(returnDate));
                        statement.setString(2, bookTitle);
                        statement.setInt(3, rollNumber);

                        int rowsAffected = statement.executeUpdate();

                        if (rowsAffected > 0) {
                            // Update quantity of the book
                            String updateQuantitySql = "UPDATE Books SET Quantity = Quantity + 1 WHERE Title = ?";
                            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuantitySql)) {
                                updateStatement.setString(1, bookTitle);
                                updateStatement.executeUpdate();
                            }

                            // Calculate the fine
                            double fine = calculateFine(returnDate, bookTitle, rollNumber, connection);

                            // Get the existing fine from the students table
                            double existingFine = getExistingFine(rollNumber, connection);

                            // Update the fine in the students table by adding the new fine to the existing
                            // fine
                            double updatedFine = existingFine + fine;
                            String updateFineSql = "UPDATE students SET Fine = ? WHERE Roll_no = ?";
                            try (PreparedStatement updateFineStatement = connection.prepareStatement(updateFineSql)) {
                                updateFineStatement.setDouble(1, updatedFine);
                                updateFineStatement.setInt(2, rollNumber);
                                updateFineStatement.executeUpdate();
                            }

                            String message = "Book returned successfully.\nFine to be paid: Rs." + updatedFine;
                            JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Error occurred while accessing the database.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }

        private double getExistingFine(int rollNumber, Connection connection) throws SQLException {
            String selectFineSql = "SELECT Fine FROM students WHERE Roll_no = ?";
            try (PreparedStatement statement = connection.prepareStatement(selectFineSql)) {
                statement.setInt(1, rollNumber);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getDouble("Fine");
                    }
                }
            }
            return 0.0; // Return 0.0 if the fine is not found or an error occurs
        }

        private double calculateFine(LocalDate returnDate, String bookTitle, Integer rollNumber, Connection connection)
                throws SQLException {
            double fine = 0.0;

            String sql = "SELECT LendingDate FROM Lending WHERE BookTitle = ? AND RollNumber = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, bookTitle);
                statement.setInt(2, rollNumber);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        LocalDate lendingDate = resultSet.getDate("LendingDate").toLocalDate();
                        long daysDiff = ChronoUnit.DAYS.between(lendingDate, returnDate);
                        if (daysDiff > 14) {
                            long daysOverdue = daysDiff - 14;
                            fine += daysOverdue * 10;
                        }
                    }
                }
            }

            return fine;
        }

        private void payFine() {

            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String selectFineSql = "SELECT Fine FROM students WHERE Roll_no = ?";
                try (PreparedStatement statement = connection.prepareStatement(selectFineSql)) {
                    statement.setInt(1, rollNumber);
                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (resultSet.next()) {
                            double currentFine = resultSet.getDouble("Fine");

                            if (currentFine > 0) {
                                JOptionPane.showMessageDialog(null, "Fine Amount: Rs." + currentFine,
                                        "Fine Details", JOptionPane.INFORMATION_MESSAGE);

                                String amountStr = JOptionPane.showInputDialog(null, "Enter Amount to Pay:",
                                        "Fine Payment",
                                        JOptionPane.PLAIN_MESSAGE);
                                if (amountStr != null) {
                                    double amountPaid = Double.parseDouble(amountStr);

                                    double remainingFine = currentFine - amountPaid;
                                    if (remainingFine >= 0) {
                                        JOptionPane.showMessageDialog(null, "Fine paid successfully.", "Success",
                                                JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        double extraChange = Math.abs(remainingFine);
                                        JOptionPane.showMessageDialog(null,
                                                "Extra change returned to the student - Rs." + extraChange,
                                                "Extra Change Returned",
                                                JOptionPane.INFORMATION_MESSAGE);
                                        remainingFine = 0;
                                    }

                                    String updateFineSql = "UPDATE students SET Fine = ? WHERE Roll_no = ?";
                                    try (PreparedStatement updateStatement = connection
                                            .prepareStatement(updateFineSql)) {
                                        updateStatement.setDouble(1, remainingFine);
                                        updateStatement.setInt(2, rollNumber);
                                        updateStatement.executeUpdate();
                                    }
                                }
                            } else {
                                JOptionPane.showMessageDialog(null, "The student does not have any fine pending.",
                                        "No Fine", JOptionPane.INFORMATION_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "No fine record found for the student.",
                                    "No Record",
                                    JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error occurred while accessing the database.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        private void showCurrentBooks() {
            try (Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD)) {
                String sql = "SELECT BookTitle, LendingDate, DATE_ADD(LendingDate, INTERVAL 14 DAY) AS DueDate, " +
                        "(DATEDIFF(CURRENT_DATE, DATE_ADD(LendingDate, INTERVAL 14 DAY)) * 10) AS Fine " +
                        "FROM History WHERE RollNumber = ? AND ReturnDate IS NULL";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, rollNumber); // Replace 'rollNumber' with the appropriate variable or method
                                                     // call

                    try (ResultSet resultSet = statement.executeQuery()) {
                        if (!resultSet.next()) {
                            JOptionPane.showMessageDialog(null, "No books currently issued.", "No Books Issued",
                                    JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        String[] columnNames = { "Book Title", "Lending Date", "Due Date", "Accumulated Fine" };
                        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

                        do {
                            String bookTitle = resultSet.getString("BookTitle");
                            Date lendingDate = resultSet.getDate("LendingDate");
                            Date dueDate = resultSet.getDate("DueDate");
                            double fine = resultSet.getDouble("Fine");
                            if (fine < 0) {
                                fine = 0;
                            }

                            model.addRow(new Object[] { bookTitle, lendingDate, dueDate, fine });
                        } while (resultSet.next());

                        JTable table = new JTable(model);
                        table.setRowSelectionAllowed(false);
                        table.setFillsViewportHeight(true);
                        JScrollPane scrollPane = new JScrollPane(table);
                        scrollPane.setPreferredSize(new Dimension(500, 300));

                        JOptionPane.showMessageDialog(null, scrollPane, "Current Books", JOptionPane.PLAIN_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error occurred while accessing the database.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                LibraryManagementSystem lms = new LibraryManagementSystem();
                lms.setVisible(true);

            }
        });
    }
}