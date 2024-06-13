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


    public void init() {
        Customer customer1 = new Customer(Math.abs(new Random().nextLong()), new ArrayList<>(), "Mairle", "Molitz", "Auf der Lauer 12", "712737", "Eichendorf", true);
        Customer customer2 = new Customer(Math.abs(new Random().nextLong()), new ArrayList<>(), "Hadar", "Quentin", "Auf der Heide 22", "71032", "Kaffka", false);

        Book book1 = new Book(UUID.randomUUID().toString(), "title1", Arrays.asList("Peter", "Quentin"), 1900, "city1", "publisher1", 0);
        Book book2 = new Book(UUID.randomUUID().toString(), "title2", Arrays.asList("Emily", "Nora"), 1900, "city2", "publisher2", 0);

        BookCopy bookCopy1 = new BookCopy(1, book1, new Date(), false);
        BookCopy bookCopy2 = new BookCopy(2, book2, new Date(), false);

        customers.addAll(Arrays.asList(customer1, customer2));
        books.addAll(Arrays.asList(book1, book2));
        bookCopies.addAll(Arrays.asList(bookCopy1, bookCopy2));

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println();

        customers.forEach(c -> System.out.println(c.toString()));

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println();

        books.forEach(b -> System.out.println(b.toString()));

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println();

        bookCopies.forEach(c -> System.out.println(c.toString()));

        System.out.println();
        System.out.println("---------------------------------------------------------");
        System.out.println();

    }
}
