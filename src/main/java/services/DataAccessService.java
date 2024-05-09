package services;

import database.Database;
import entities.Book;
import entities.BookCopy;
import entities.Customer;
import exceptions.BookCopyNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.CustomerNotFoundException;

public class DataAccessService {

    Database database = Database.INSTANCE;

    public void deleteCustomer(long id) throws CustomerNotFoundException {
        Customer customer = database.getCustomers().stream()
                .filter(c -> c.getId() == (id))
                .findFirst()
                .orElseThrow(() -> new CustomerNotFoundException(String.format("customer with id: %d cannot be found", id)));

        if (!customer.isFeesPayed() || !customer.getBookCopies().isEmpty())
            throw new IllegalStateException("customer has unpaid fees or borrowed books");

        database.getCustomers().remove(customer);
        System.out.println("customer has been deleted");

    }

    public void deleteBook(String isbn) throws BookNotFoundException {
        Book bookToDelete = database.getBooks().stream()
                .filter(b -> b.getIsbn().equals(isbn))
                .findFirst()
                .orElseThrow(() -> new BookNotFoundException(String.format("book with ISBN %s not found", isbn)));

        boolean anyCopyLent = database.getBookCopies().stream()
                .filter(c -> c.getBook().equals(bookToDelete))
                .anyMatch(BookCopy::isLent);

        if (anyCopyLent)
            throw new IllegalStateException("at least one copy of this book is currently lent");

        database.getBookCopies().removeIf(c -> c.getBook().equals(bookToDelete));
        database.getBooks().remove(bookToDelete);
        System.out.println("book and corresponding copies have been deleted");
    }

    public void deleteBookCopy(long id) throws BookCopyNotFoundException {
        BookCopy copy = database.getBookCopies().stream()
                .filter(c -> c.getId() == (id))
                .findFirst()
                .orElseThrow(() -> new BookCopyNotFoundException(String.format("bookCopy with id %d cannot be found", id)));

        if (copy.isLent())
            throw new IllegalStateException("bookcopy is currently lent");

        database.getBookCopies().remove(copy);
        System.out.println("copy has been deleted");
    }
}
