package services;

import database.Database;
import entities.BookCopy;
import exceptions.CustomerNotFoundException;

/**
 * This class provides reporting functionalities for the library system.
 * It offers methods to display information about books, customers,
 * borrowed and available book copies, and a customer's borrowed copies.
 */
public class ReportingService {

    /**
     * Prints information about all books in the library system.
     */
    public void issueAllBooks() {
        Database.INSTANCE.getBooks()
                .forEach(System.out::println);
    }

    /**
     * Prints information about all customers registered in the library system.
     */
    public void issueCustomers() {
        Database.INSTANCE.getCustomers()
                .forEach(System.out::println);
    }

    /**
     * Prints information about all book copies that are currently not lent out.
     */
    public void issueNotLentCopies() {
        Database.INSTANCE.getBookCopies()
                .stream()
                .filter(book -> !book.isLent())
                .forEach(System.out::println);
    }

    /**
     * Prints information about all book copies that are currently lent out.
     */
    public void issueLentCopies() {
        Database.INSTANCE.getBookCopies()
                .stream()
                .filter(BookCopy::isLent)
                .forEach(System.out::println);
    }

    /**
     * Prints information about all book copies currently borrowed by a customer identified by the provided customerId.
     *
     * @param customerId The unique identifier of the customer.
     * @throws RuntimeException / {@link CustomerNotFoundException} If a customer with the provided ID cannot be found in the database.
     */
    public void issueCustomersCopies(long customerId) {
        Database.INSTANCE.getCustomers()
                .stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .ifPresentOrElse(
                        customer -> System.out.println(customer.getBookCopies()),
                        () -> { throw new RuntimeException(new CustomerNotFoundException("customerId does not exist")); }
                );
    }
}
