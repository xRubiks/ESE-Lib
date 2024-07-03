package database;

import entities.Book;
import entities.BookCopy;
import entities.Customer;

import java.util.*;

public enum Database {

    INSTANCE;

    private final List<Book> books = new ArrayList<>();
    private final List<BookCopy> bookCopies = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    public List<Book> getBooks() {
        return books;
    }

    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void sortDB() {
        if(!bookCopies.isEmpty())
            bookCopies.sort(Comparator.comparing(bookCopy -> bookCopy.getBook().getTitle()));
        if(!books.isEmpty())
            books.sort(Comparator.comparing(Book::getTitle));
        if(!customers.isEmpty())
            customers.sort(Comparator.comparing(Customer::getLastName).thenComparing(Customer::getFirstName));
    }
}
