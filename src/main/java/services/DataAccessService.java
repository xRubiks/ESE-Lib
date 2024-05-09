package services;

import database.Database;
import entities.Book;
import entities.BookCopy;
import entities.Customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataAccessService {

    Database database = Database.INSTANCE;

    public void deleteCustomer(long id) {
        Optional<Customer> customer = database.getCustomers().stream().filter(c -> c.getId() == id).findFirst();
        if (!customer.isPresent()) {
//            throw TODO: Implement CustomerNotFoundException;
        }
        if (!customer.get().isFeesPayed() || !customer.get().getBookCopies().isEmpty()) {
//          TODO : Implement Guy can not be deleted Exception
            return;
        }
        database.getCustomers().remove(customer);


    }

    public void deleteBook(String isbn) {
        Optional<Book> book = database.getBooks().stream().filter(b -> b.getIsbn().equals(isbn)).findFirst();
        if (!book.isPresent()) {
//            throw TODO: Implement BookNotFoundException;
        }
        Stream<BookCopy> copies = database.getBookCopies().stream().filter(c1 -> c1.getBook().equals(book.get()));
        if (copies.anyMatch(BookCopy::isLent)) {
//            TODO : Implement Book can't be deleted
        }
        database.getBookCopies().removeAll(copies.toList());
        database.getBooks().remove(book);


    }

    public void deleteBookCopy(long id) {
        Optional<BookCopy> copy = database.getBookCopies().stream().filter(bookCopy -> bookCopy.getId() == id).findFirst();
        if (!copy.isPresent()) {
//            throw TODO: Implement BookNotFoundException;
        } else if (copy.get().isLent()) {
            //TODO Implement BookCantBeDeleted Exception
        }
        database.getBookCopies().remove(copy);
    }
}
