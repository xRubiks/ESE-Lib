package database;

import entities.Book;
import entities.BookCopy;
import entities.Customer;

import java.util.ArrayList;
import java.util.List;

public class Database {

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
}
