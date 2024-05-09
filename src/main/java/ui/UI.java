package ui;

import services.DataAccessService;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class for the UI
 */
public class UI {
    private final Scanner scanner;
    private boolean running = true;


    public UI(){
        scanner = new Scanner(System.in);
        start();
    }

    /**
     * The function keeping the project running as long as the user does not want to exit
     */
    private void start(){
        while(running){
            mainMenu();
        }
    }


    /**
     * Method which displays the main menu on the console
     */
    private void mainMenu(){
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
    private void selectOptionInMainMenu(){
        int option = getIntInputMinMax(1,6);
        switch (option){
            case 1 -> lendBook();
            case 2 -> returnBook();
            case 3 -> searchBook();
            case 4 -> manageBooks(); //TODO write submenu
            case 5 -> manageCustomers(); //TODO write submenu
            case 6 -> exit();
        }
    }


    /**
     * Getter for int input with a minimum and maximum
     * @param min the minimum number allowed
     * @param max the maximum number allowed
     * @return the inputted number if it is valid
     */
    private int getIntInputMinMax(int min, int max){
        boolean inputValid = false;
        int input = 0;
        while(!inputValid){
            try {
                input = scanner.nextInt();
                inputValid = true;
            }catch (InputMismatchException exc){
                System.out.format("The input must be a number between %d and %d\n", min, max);
            } finally{
                scanner.nextLine();
            }
            if(input < min || input > max){
                System.out.format("Please enter a number between %d and %d\n", min, max);
                inputValid = false;
            }
        }
        return input;
    }

    /**
     * Method to display the submenu for searching books in the console
     */
    private void searchBook(){
        System.out.println("How would you like to search for the book? \n");
        System.out.println("[1] Book Title");
        System.out.println("[2] Book Author");
        System.out.println("[3] ISBN");

        System.out.print("Enter a number between 1 and 3: "); //TODO: bessere Formulierung finden

        selectOptionSearchMenu();
    }

    /**
     * Gets a user input int between 1 and 6 and redirects to the corresponding
     * option
     */
    private void selectOptionSearchMenu() {
        int option = getIntInputMinMax(1,3);
        switch (option){
            case 1 -> searchBookByTitle();
            case 2 -> searchBookByAuthor();
            case 3 -> searchBookByISBN();
        }
    }

    /**
     * Demo method for lending books
     */
    private void lendBook(){
        demo();
    }

    /**
     * Demo method for returning books
     */
    private void returnBook(){
        demo();
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
        System.out.println("[7] Get a list of all currently lent books \n");

        System.out.format("To choose, enter a number between 1 and 7: "); //TODO: bessere Formulierungen finden

        selectOptionBooksMenu();
    }

    private void selectOptionBooksMenu() {
        int option = getIntInputMinMax(1,7);
        switch (option) {
            case 1 -> demo();
            case 2 -> {
                System.out.print("enter ISBN: ");
                try { new DataAccessService().deleteBook(scanner.nextLine()); } catch (Exception ignored) { } }
            case 3 -> demo();
            case 4 -> {
                System.out.print("enter ID: ");
                try { new DataAccessService().deleteBookCopy(scanner.nextLong()); } catch (Exception ignored) {} }
            case 5 -> demo();
            case 6 -> demo();
            case 7 -> demo();
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
    }

    private void selectOptionCustomerMenu(){
        int option = getIntInputMinMax(1,4);
        switch (option) {
            case 1 -> demo();
            case 2 -> {
                System.out.print("enter ID: ");
                try { new DataAccessService().deleteCustomer(scanner.nextLong()); } catch (Exception ignored) {} }
            case 3 -> demo();
            case 4 -> demo();
        }
    }

    /**
     * demo method for searching a book by it`s title
     */
    private void searchBookByTitle(){
        demo();
    }

    /**
     * Demo method for searching a book by it`s author
     */
    private void searchBookByAuthor(){
        demo();
    }

    /**
     * Demo method for searching a book by it`s ISBN
     */
    private void searchBookByISBN(){
        demo();
    }


    /**
     * Method to exit the program
     */
    private void exit() {
        running = false;
    }


    /**
     * Method displaying a demo message and redirecting back to the main menu
     */
    private void demo(){
        System.out.println("\nThis function has not yet been implemented,\nyou will be returned to " +
                "the main menu\n");
        System.out.println("Press enter to continue...");
        scanner.nextLine();
    }

}
