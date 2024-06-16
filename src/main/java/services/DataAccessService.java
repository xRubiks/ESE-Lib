package services;

import database.Database;
import entities.BookCopy;
import entities.Customer;
import exceptions.BookCopyNotFoundException;
import exceptions.CustomerNotFoundException;
import exceptions.InvalidStateException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * This class provides functionalities for searching book copies.
 */
public class DataAccessService {

    /**
     * Searches for book copies based on the provided title.
     *
     * @param title The title of the book to search for.
     * @return A list of {@link BookCopy} objects that match the provided title.
     */
    public List<BookCopy> searchBookCopyByTitle(String title) {
        return Database.INSTANCE.getBookCopies()
                .stream()
                .filter(bookCopy -> Objects.equals(bookCopy.getBook().getTitle(), title))
                .toList();
    }

    /**
     * Searches for book copies based on the provided author's name.
     * This method searches for book copies where the author's name is present in the book's author list.
     *
     * @param author The name of the author to search for.
     * @return A list of {@link BookCopy} objects where the provided author is listed in the book.
     */
    public List<BookCopy> searchBookCopyByAuthor(String author) {
        return Database.INSTANCE.getBookCopies()
                .stream()
                .filter(copy -> copy.getBook().getAuthors().contains(author))
                .toList();
    }

    /**
     * Searches for a specific book copy by its isbn.
     *
     * @param isbn The unique identifier of the book to search for.
     * @return A list of {@link BookCopy} objects with the provided ISBN.
     */
    public List<BookCopy> searchBookCopyByISBN(String isbn) {
        return Database.INSTANCE.getBookCopies()
                .stream()
                .filter(copy -> copy.getBook().getIsbn().equals(isbn))
                .toList();
    }

    /**
     * Lends a book copy to a customer.
     *
     * @param customerId The ID of the customer.
     * @param bookCopyId The ID of the book copy to be lent.
     * @throws BookCopyNotFoundException If the book copy with the given ID is not found.
     * @throws CustomerNotFoundException If the customer with the given ID is not found.
     * @throws InvalidStateException     If the customer has unpaid fees, already has more than 5 book copies, or the book copy is already lent.
     */
    public void lendBookCopy(long customerId, long bookCopyId) throws BookCopyNotFoundException, CustomerNotFoundException, InvalidStateException {
        BookCopy bookCopy = Database.INSTANCE.getBookCopies()
                .stream()
                .filter(copy -> copy.getId() == bookCopyId)
                .findFirst()
                .orElseThrow(() -> new BookCopyNotFoundException(String.format("could not find book with id: %d", bookCopyId)));
        Customer customer = Database.INSTANCE.getCustomers()
                .stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(String.format("could not find customer with id: %d", customerId)));

        if (customer.getBookCopies().size() >= 5)
            throw new InvalidStateException("Customer has more than copies than allowed");

        if (!customer.isFeesPayed())
            throw new InvalidStateException("Customer has not payed fees");

        if (bookCopy.isLent())
            throw new InvalidStateException("BookCopy is already lent");

        bookCopy.setLentDate(new Date());
        bookCopy.setLent(true);
        customer.getBookCopies().add(bookCopy);
     }

    /**
     * Returns a book copy from a customer.
     *
     * @param customerId The ID of the customer.
     * @param bookCopyId The ID of the book copy to be returned.
     * @throws BookCopyNotFoundException If the book copy with the given ID is not found.
     * @throws CustomerNotFoundException If the customer with the given ID is not found.
     */
    public void returnBookCopy(long customerId, long bookCopyId) throws BookCopyNotFoundException, CustomerNotFoundException {
        BookCopy copy = Database.INSTANCE.getBookCopies()
                .stream()
                .filter(c -> c.getId() == bookCopyId)
                .findFirst()
                .orElseThrow(() -> new BookCopyNotFoundException(String.format("could not find copy with id: %d", bookCopyId)));

        Customer customer = Database.INSTANCE.getCustomers()
                .stream()
                .filter(c -> c.getId() == customerId)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(String.format("could not find customer with id: %d", customerId)));

        if (copy.getLentDate() != null) {
            long daysSinceLent = ChronoUnit.DAYS.between(copy.getLentDate().toInstant(), Instant.now());
            if (daysSinceLent > 21) {
                customer.setFeesPayed(false);
                System.out.println("Customer needs to pay fees");
            }
        }

        customer.getBookCopies().remove(copy);
        copy.setLent(false);
        copy.setLentDate(null);
        Database.INSTANCE.sortDB();
    }


}

