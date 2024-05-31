# Library Management System

This program is a tool for a command line user interaction for managing books, book copies, and customers in a library.

## Features

- Lend and return book copies
- Search for books by title, author, or ISBN
- Manage books and customers
- Import/export data via CSV files
- View lists of all books, all book copies, and all customers

## Compiling and Running the Java Application

To compile and run the Java application using IntelliJ IDEA, please follow these steps:

1. Open Terminal

2. Navigate to the project directory using the `cd` command.

3. To compile the Java application, use the `mvn clean compile assembly:single` command.

4. To run the Java application, use the `java -jar .\target\project07-Lib1-V2-jar-with-dependencies.jar` command.

## Usage

To use the program, follow these steps:

1. Run the program.
2. Select an option from the main menu by entering the corresponding number.
3. Follow the given instructions to complete the wanted action.
4. Repeat steps 2-3 for further actions, or choose the "Exit and save data" option to close the program.

## Main Menu Options

1. **Lend book copy**: Borrow a book copy from the library.
2. **Return book copy**: Return a borrowed book copy to the library.
3. **Search book copy**: Search for a book copy in the library by title, author, or ISBN.
4. **Manage books**: Perform different actions related to managing books, such as importing/exporting data from customers and deleting books.
5. **Manage customers**: Perform different actions related to managing customers, such as importing/exporting data and deleting customers.
6. **Exit and save data**: Close the program and save any changes made during the user interaction.


## Running the Tests

To run tests for the Java application using IntelliJ IDEA, please follow these steps:

1. Open Terminal

2. Navigate to the project directory using the `cd` command.

3. To run all tests, use the `mvn test` command.

## Runnning Test Coverage

1. Open Terminal
2. Navigate to the project directory using the `cd` command.
3. To generate a test coverage report, use the `mvn clean verify` command.
4. Open the `index.html` file using `.\target\site\jacoco\index.html` to view the test coverage report.
