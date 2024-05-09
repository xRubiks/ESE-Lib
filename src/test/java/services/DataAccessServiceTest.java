package services;

import database.Database;
import entities.Book;
import entities.BookCopy;
import entities.Customer;
import exceptions.BookCopyNotFoundException;
import exceptions.BookNotFoundException;
import exceptions.CustomerNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DataAccessServiceTest {

    private final DataAccessService dataAccessService = new DataAccessService();
    private BookCopy copy1;
    
    @BeforeEach
    public void setup() {
        Database.INSTANCE.getCustomers().clear();
        Database.INSTANCE.getBooks().clear();
        Database.INSTANCE.getBookCopies().clear();

        Customer customer1 = new Customer(1, new ArrayList<>());
        Customer customer2 = new Customer(2, new ArrayList<>());

        Book book1 = new Book("1", "title1", Arrays.asList("Peter", "Quentin"), 1900, "city1", "publisher1", 0);
        Book book2 = new Book("2", "title2", Arrays.asList("Emily", "Nora"), 1900, "city2", "publisher2", 0);

        BookCopy bookCopy1 = new BookCopy(1, book1);
        BookCopy bookCopy2 = new BookCopy(2, book2);

        copy1 = bookCopy1;
        
        Database.INSTANCE.getCustomers().addAll(Arrays.asList(customer1, customer2));
        Database.INSTANCE.getBooks().addAll(Arrays.asList(book1, book2));
        Database.INSTANCE.getBookCopies().addAll(Arrays.asList(bookCopy1, bookCopy2));;
    }

    @Test
    public void deleteCustomerWorksRight() {
        try {
            dataAccessService.deleteCustomer(1);
        } catch (Exception ignored) {}

        assertTrue(Database.INSTANCE.getCustomers().stream().noneMatch(c -> c.getId() == 1));
    }

    @Test
    public void deleteCustomerIdCouldntFind() {
        assertThrows(CustomerNotFoundException.class, () -> dataAccessService.deleteCustomer(10)) ;

    }

    @Test
    public void deleteCustomerFeesUnpaid() {
        Database.INSTANCE.getCustomers().get(0).setFeesPayed(false);
        assertThrows(IllegalStateException.class, () -> dataAccessService.deleteCustomer(1));
    }
    
    @Test
    public void deleteCustomerCopiesLent() {
        Database.INSTANCE.getCustomers().get(0).getBookCopies().add(copy1);
        assertThrows(IllegalStateException.class, () -> dataAccessService.deleteCustomer(1));
    }

    @Test
    public void deleteBookCopyWorksRight(){
        try {
            dataAccessService.deleteBookCopy(1);
        } catch (Exception ignored) {}
        assertTrue(Database.INSTANCE.getBookCopies().stream().noneMatch(bc -> bc.getId() == 1));
    }

    @Test
    public void deleteBookCopyIdCouldntFind(){
        assertThrows(BookCopyNotFoundException.class, () -> dataAccessService.deleteBookCopy(Long.MIN_VALUE));
    }

    @Test
    public void deleteBookCopyIsLent(){
        copy1.setLent(true);
        assertThrows(IllegalStateException.class, () -> dataAccessService.deleteBookCopy(1));
    }

    @Test
    public void deleteBookWorksRight(){
        try {
            dataAccessService.deleteBook("1");
        } catch (Exception ignored) {}
        assertTrue(Database.INSTANCE.getBooks().stream().noneMatch(b -> b.getIsbn().equals("1")));
    }

    @Test
    public void deleteBookIsbnNotFound(){
        assertThrows(BookNotFoundException.class, () -> dataAccessService.deleteBook(String.valueOf(6)));
    }

    @Test
    public void deleteBookIsLent(){
        copy1.setLent(true);
        assertThrows(IllegalStateException.class, () -> dataAccessService.deleteBook("1"));
    }

}