package ui;

import services.CSVService;
import services.DataAccessService;
import services.RemovalService;
import services.ReportingService;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class for the UI
 */
public class UI {
    private final Scanner scanner;
    private boolean running = true;


    public UI() {
        scanner = new Scanner(System.in);
        start();
    }

    /**
     * The function keeping the project running as long as the user does not want to exit
     */
    private void start() {
        while (running) {
            mainMenu();
        }
    }


    /**
     * Method which displays the main menu on the console
     */
    private void mainMenu() {
        System.out.println("Main Menu: \n");
        System.out.println("[1] Lend book copy");
        System.out.println("[2] Return book copy");
        System.out.println("[3] Search book copy");
        System.out.println("[4] Manage books");
        System.out.println("[5] Manage customers");
        System.out.println("[6] Exit and save data");
        System.out.println("An option may be selected by typing a number between 1 and 6 \n");

        System.out.print("The number corresponding to the option you would like to choose: "); //TODO: bessere Formulierung hierfür finden
        selectOptionInMainMenu();
        System.out.println("\n");
    }


    /**
     * Gets a user input int between 1 and 6 and redirects to the corresponding
     * submenu
     */
    private void selectOptionInMainMenu() {
        int option = getIntInputMinMax(1, 6);
        switch (option) {
            case 1 -> {
                System.out.println("enter customer ID: ");
                long customerID = scanner.nextLong();
                System.out.println("enter book copy ID ");
                long bookCopyID = scanner.nextLong();
                try {
                    new DataAccessService().lendBookCopy(customerID, bookCopyID);
                } catch (Exception ignored) {
                } finally {
                    scanner.nextLine();
                }
            }
            case 2 -> {
                System.out.println("enter customer ID: ");
                long customerID = scanner.nextLong();
                System.out.println("enter book copy ID: ");
                long bookCopyID = scanner.nextLong();
                try {
                    new DataAccessService().returnBookCopy(customerID, bookCopyID);
                } catch (Exception ignored) {
                } finally {
                    scanner.nextLine();
                }
            }
            case 3 -> {
                searchBook();

            }
            case 4 -> manageBooks();
            case 5 -> manageCustomers();
            case 6 -> exit();
        }
    }


    /**
     * Getter for int input with a minimum and maximum
     *
     * @param min the minimum number allowed
     * @param max the maximum number allowed
     * @return the inputted number if it is valid
     */
    private int getIntInputMinMax(int min, int max) {
        boolean inputValid = false;
        int input = 0;
        while (!inputValid) {
            try {
                input = scanner.nextInt();
                inputValid = true;
            } catch (InputMismatchException exc) {
                System.out.format("The input must be a number between %d and %d\n", min, max);
            } finally {
                scanner.nextLine();
            }
            if (input < min || input > max) {
                System.out.format("Please enter a number between %d and %d\n", min, max);
                inputValid = false;
            }
        }
        return input;
    }

    /**
     * Method to display the submenu for searching books in the console
     */
    private void searchBook() {
        System.out.println("How would you like to search for the book? \n");
        System.out.println("[1] Book Title");
        System.out.println("[2] Book Author");
        System.out.println("[3] ISBN");

        System.out.print("Enter a number between 1 and 3: "); //TODO: bessere Formulierung finden

        selectOptionSearchMenu();

        clearCommandLine();
    }

    /**
     * Gets a user input int between 1 and 6 and redirects to the corresponding option
     */
    private void selectOptionSearchMenu() {
        int option = getIntInputMinMax(1, 3);
        switch (option) {
            case 1 -> {
                System.out.println("enter book author: ");
                System.out.println(new DataAccessService().searchBookCopyByAuthor(scanner.nextLine()));
            }
            case 2 -> {
                System.out.println("enter title: ");
                System.out.println(new DataAccessService().searchBookCopyByTitle(scanner.nextLine()));
            }
            case 3 -> {
                System.out.println("enter ISBN: ");
                System.out.println(new DataAccessService().searchBookCopyByISBN(scanner.nextLine()));
            }
        }
    }

    /**
     * Demo method for managing books
     */
    private void manageBooks() {
        System.out.println("What would you like to do?\n");
        System.out.println("[1] Import CSV-File with books");
        System.out.println("[2] Delete a book via it`s ISBN");
        System.out.println("[3] Import a CSV-file with book copies");
        System.out.println("[4] Delete a book copy via it`s ID");
        System.out.println("[5] Get a list of all books");
        System.out.println("[6] Get a list of all currently not lent book copies");
        System.out.println("[7] Get a list of all currently lent books");
        System.out.println("[8] Get a list of the number of book copies per publisher \n");

        System.out.format("To choose, enter a number between 1 and 8: ");

        selectOptionBooksMenu();

        System.out.println("Press Enter to continue...");
        scanner.nextLine();

        clearCommandLine();

    }

    private void clearCommandLine() {
        System.out.println("---------------------------------------------");
        for (int i = 0; i <= 5; i++)
            System.out.println();
        System.out.println("---------------------------------------------");
        System.out.println();

    }

    private void selectOptionBooksMenu() {
        int option = getIntInputMinMax(1, 8);
        switch (option) {
            case 1 -> {
                System.out.println("enter filepath: ");
                try {
                    new CSVService().importBooksViaCSV(scanner.nextLine());
                } catch (Exception ignored) {
                } finally {
                    scanner.nextLine();
                }
            }
            case 2 -> {
                System.out.print("enter ISBN: ");
                try {
                    new RemovalService().deleteBook(scanner.nextLine());
                } catch (Exception ignored) {
                } finally {
                    scanner.nextLine();
                }
            }
            case 3 -> {
                System.out.println("enter filepath: ");
                try {
                    new CSVService().importBookCopiesViaCSV(scanner.nextLine());
                } catch (Exception ignored) {
                } finally {
                    scanner.nextLine();
                }
            }
            case 4 -> {
                System.out.print("enter ID: ");
                try {
                    new RemovalService().deleteBookCopy(scanner.nextLong());
                } catch (Exception ignored) {
                } finally {
                    scanner.nextLine();
                }
            }
            case 5 -> new ReportingService().printAllBooks();
            case 6 -> new ReportingService().printAllNonLentCopies();
            case 7 -> new ReportingService().printAllLentCopies();
            case 8 -> new ReportingService().printBooksPerPublisher();

        }
    }

    /**
     * Demo method for managing customers
     */
    private void manageCustomers() {
        System.out.println("What would you like to do?\n");
        System.out.println("[1] Import a CSV-file with customers");
        System.out.println("[2] Delete a customer via their ID");
        System.out.println("[3} Get a list of customers");
        System.out.println("[4] Get a list of all borrowed books of a customer \n");

        System.out.println("To choose an option, enter a number between 1 and 4");

        selectOptionCustomerMenu();

        System.out.println("Press Enter to continue...");
        scanner.nextLine();

        clearCommandLine();
    }

    private void selectOptionCustomerMenu() {
        int option = getIntInputMinMax(1, 4);
        switch (option) {
            case 1 -> {
                System.out.println("enter filepath: ");
                try {
                    new CSVService().importCustomersViaCSV(scanner.nextLine());
                } catch (Exception ignored) {
                } finally {
                    scanner.nextLine();
                }
            }
            case 2 -> {
                System.out.print("enter ID: ");
                try {
                    new RemovalService().deleteCustomer(scanner.nextLong());
                } catch (Exception ignored) {
                } finally {
                    scanner.nextLine();
                }
            }
            case 3 -> new ReportingService().printAllCustomers();
            case 4 -> {
                System.out.println("enter customer ID: ");
                new ReportingService().printAllCustomersCopies(scanner.nextLong());
            }
        }
    }

    /**
     * Method to exit the program
     */
    private void exit() {
        running = false;
    }

}
