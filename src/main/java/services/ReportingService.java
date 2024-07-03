package services;

import database.Database;
import entities.BookCopy;
import exceptions.CustomerNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * This class provides reporting functionalities for the library system.
 * It offers methods to display information about books, customers,
 * borrowed and available book copies, and a customer's borrowed copies.
 */
public class ReportingService {

    /**
     * Prints information about all books in the library system.
     */
    public void printAllBooks() {
        Database.INSTANCE.getBooks()
                .forEach(System.out::println);
    }

    /**
     * Prints information about all customers registered in the library system.
     */
    public void printAllCustomers() {
        Database.INSTANCE.getCustomers()
                .forEach(System.out::println);
    }

    /**
     * Prints information about all book copies that are currently not lent out.
     */
    public void printAllNonLentCopies() {
        Database.INSTANCE.getBookCopies()
                .stream()
                .filter(book -> !book.isLent())
                .forEach(System.out::println);
    }

    /**
     * Prints information about all book copies that are currently lent out.
     */
    public void printAllLentCopies() {
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
    public void printAllCustomersCopies(long customerId) {
        Database.INSTANCE.getCustomers()
                .stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .ifPresentOrElse(
                        customer -> System.out.println(customer.getBookCopies()),
                        () -> {
                            throw new RuntimeException(new CustomerNotFoundException("customerId does not exist"));
                        }
                );
    }

    /**
     * Prints the number and percentage of book copies per publisher in alphabetical order.
     * Additionally, prints the count and percentage of book copies from publishers not included in the given list.
     */
    public void printBooksPerPublisher() {
        List<String> publishers = new ArrayList<>();
        Database.INSTANCE.getBooks().stream().forEach(book -> {
            if (!publishers.contains(book.getPublisher())) {
                publishers.add(book.getPublisher());
            }
        });
        int numberOfCopies = Database.INSTANCE.getBookCopies().size();
        Collections.sort(publishers);
        if (numberOfCopies != 0) {
            for (String publisher : publishers) {
                int number = Database.INSTANCE.getBookCopies().stream()
                        .filter(copy -> copy.getBook().getPublisher().equals(publisher)).toList().size();
                double percentage = (double) number / numberOfCopies * 100;
                System.out.printf(Locale.GERMANY,"%s: %d copies (%.1f%%)%n", publisher, number, percentage);
            }
            int other = Database.INSTANCE.getBookCopies().stream()
                    .filter(copy -> !publishers.contains(copy.getBook().getPublisher())).toList().size();
            if (other != 0) {
                double percentage = (double) other / numberOfCopies * 100;
                System.out.printf(Locale.GERMANY,"Other publishers: %d copies (%.1f%%)%n", other, percentage);
            }
        }
    }

}
