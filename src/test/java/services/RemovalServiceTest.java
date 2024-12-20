package services;

import database.Database;
import entities.Book;
import entities.BookCopy;
import entities.Customer;
import exceptions.BookCopyNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.CustomerNotFoundException;
import exceptions.InvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class RemovalServiceTest {

    private final RemovalService dataAccessService = new RemovalService();
    private BookCopy copy1;

    @BeforeEach
    public void setup() {
        Database.INSTANCE.getCustomers().clear();
        Database.INSTANCE.getBooks().clear();
        Database.INSTANCE.getBookCopies().clear();

        Customer customer1 = new Customer(1, new ArrayList<>(), "Mairle", "Molitz", "Auf der Lauer 12", "712737", "Kafka", true);
        Customer customer2 = new Customer(2, new ArrayList<>(), "Hadar", "Quentin", "Auf der Heide 22", "71032", "Böplingen", false);

        Book book1 = new Book("1", "title1", Arrays.asList("Peter", "Quentin"), 1900, "city1", "publisher1", 0);
        Book book2 = new Book("2", "title2", Arrays.asList("Emily", "Nora"), 1900, "city2", "publisher2", 0);

        BookCopy bookCopy1 = new BookCopy(1, book1, new Date(), false);
        BookCopy bookCopy2 = new BookCopy(2, book2, new Date(), false);
        BookCopy bookCopy3 = new BookCopy(3, book1, new Date(), false);

        copy1 = bookCopy1;

        Database.INSTANCE.getCustomers().addAll(Arrays.asList(customer1, customer2));
        Database.INSTANCE.getBooks().addAll(Arrays.asList(book1, book2));
        Database.INSTANCE.getBookCopies().addAll(Arrays.asList(bookCopy1, bookCopy2, bookCopy3));;
    }

    @Test
    public void deleteCustomerWorksRight() {
        try { dataAccessService.deleteCustomer(1); } catch (Exception ignored) {}
        assertTrue(Database.INSTANCE.getCustomers().stream().noneMatch(c -> c.getId() == 1));
    }

    @Test
    public void deleteCustomerIdCouldNotFind() {
        assertThrows(CustomerNotFoundException.class, () -> dataAccessService.deleteCustomer(10));
        assertEquals(2, Database.INSTANCE.getCustomers().size());
    }

    @Test
    public void deleteCustomerFeesUnpaid() {
        Database.INSTANCE.getCustomers().get(0).setFeesPayed(false);
        assertThrows(InvalidStateException.class, () -> dataAccessService.deleteCustomer(1));
        assertTrue(Database.INSTANCE.getCustomers().stream().anyMatch(c -> c.getId() == 1));
    }

    @Test
    public void deleteCustomerCopiesLent() {
        Database.INSTANCE.getCustomers().get(0).getBookCopies().add(copy1);
        assertThrows(InvalidStateException.class, () -> dataAccessService.deleteCustomer(1));
        assertTrue(Database.INSTANCE.getCustomers().stream().anyMatch(c -> c.getId() == 1));
    }

    @Test
    public void deleteBookCopyWorksRight(){
        try { dataAccessService.deleteBookCopy(1); } catch (Exception ignored) {}
        assertTrue(Database.INSTANCE.getBookCopies().stream().noneMatch(bc -> bc.getId() == 1));
    }

    @Test
    public void deleteBookCopyIdCouldNotFind(){
        assertThrows(BookCopyNotFoundException.class, () -> dataAccessService.deleteBookCopy(Long.MIN_VALUE));
        assertEquals(3, Database.INSTANCE.getBookCopies().size());
    }

    @Test
    public void deleteBookCopyIsLent(){
        copy1.setLent(true);
        assertThrows(InvalidStateException.class, () -> dataAccessService.deleteBookCopy(1));
        assertTrue(Database.INSTANCE.getBookCopies().stream().anyMatch(c -> c.getId() == 1));
    }

    @Test
    public void deleteBookWorksRight(){
        try {
            dataAccessService.deleteBook("1");
        } catch (Exception ignored) {}
        assertTrue(Database.INSTANCE.getBooks().stream().noneMatch(b -> b.getIsbn().equals("1")));
        assertTrue(Database.INSTANCE.getBookCopies().stream().noneMatch(c -> c.getBook().getIsbn().equals("1")));
    }

    @Test
    public void deleteBookIsbnNotFound(){
        assertThrows(BookNotFoundException.class, () -> dataAccessService.deleteBook(String.valueOf(6)));
        assertEquals(2, Database.INSTANCE.getBooks().size());
    }

    @Test
    public void deleteBookIsLent(){
        copy1.setLent(true);
        assertThrows(InvalidStateException.class, () -> dataAccessService.deleteBook("1"));
        assertTrue(Database.INSTANCE.getBooks().stream().anyMatch(b -> b.getIsbn().equals("1")));
    }

}