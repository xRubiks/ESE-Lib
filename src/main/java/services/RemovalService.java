package services;

import database.Database;
import entities.Book;
import entities.BookCopy;
import entities.Customer;
import exceptions.BookCopyNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.CustomerNotFoundException;
import exceptions.InvalidStateException;

/**
 * This class provides methods to interact with the data access layer,
 * allowing manipulation of customer, book, and book copy data.
 */
public class RemovalService {

    Database database = Database.INSTANCE;

    /**
     * Deletes a customer from the database.
     *
     * @param id The ID of the customer to be deleted.
     * @throws CustomerNotFoundException If no customer with the given ID is found.
     * @throws InvalidStateException    If the customer has unpaid fees or borrowed books.
     */
    public void deleteCustomer(long id) throws CustomerNotFoundException, InvalidStateException {
        Customer customer = database.getCustomers().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(String.format("Customer with ID %d cannot be found", id)));

        if (!customer.isFeesPayed() || !customer.getBookCopies().isEmpty())
            throw new InvalidStateException("Customer has unpaid fees or borrowed books");

        database.getCustomers().remove(customer);
        System.out.println("Customer has been deleted");
    }

    /**
     * Deletes a book from the database along with its copies.
     *
     * @param isbn The ISBN of the book to be deleted.
     * @throws BookNotFoundException    If no book with the given ISBN is found.
     * @throws InvalidStateException   If at least one copy of the book is currently lent.
     */
    public void deleteBook(String isbn) throws BookNotFoundException, InvalidStateException {
        Book bookToDelete = database.getBooks().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(String.format("Book with ISBN %s not found", isbn)));

        boolean anyCopyLent = database.getBookCopies().stream()
                .filter(c -> c.getBook().equals(bookToDelete))
                .anyMatch(BookCopy::isLent);

        if (anyCopyLent)
            throw new InvalidStateException("At least one copy of this book is currently lent");

        database.getBookCopies().removeIf(c -> c.getBook().equals(bookToDelete));
        database.getBooks().remove(bookToDelete);
        System.out.println("Book and corresponding copies have been deleted");
    }

    /**
     * Deletes a book copy from the database.
     *
     * @param id The ID of the book copy to be deleted.
     * @throws BookCopyNotFoundException If no book copy with the given ID is found.
     * @throws InvalidStateException    If the book copy is currently lent.
     */
    public void deleteBookCopy(long id) throws BookCopyNotFoundException, InvalidStateException {
        BookCopy copy = database.getBookCopies().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElseThrow(() -> new BookCopyNotFoundException(String.format("Book copy with ID %d cannot be found", id)));

        if (copy.isLent())
            throw new InvalidStateException("Book copy is currently lent");

        database.getBookCopies().remove(copy);
        System.out.println("Copy has been deleted");
    }

}
