package services;

import database.Database;
import entities.Book;
import entities.BookCopy;
import entities.Customer;
import exceptions.BookCopyNotFoundException;
import exceptions.CustomerNotFoundException;
import exceptions.InvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataAccessServiceTest {

    DataAccessService dataAccessService = new DataAccessService();
    BookCopy copy1;
    Customer cust1;

    @BeforeEach
    public void setup() {
        Database.INSTANCE.getCustomers().clear();
        Database.INSTANCE.getBooks().clear();
        Database.INSTANCE.getBookCopies().clear();


        Customer customer1 = new Customer(1, new ArrayList<>(), "Mairle" , "Molitz");
        Customer customer2 = new Customer(2, new ArrayList<>(), "Hadar", "Quentin");
        cust1 = customer1;

        Book book1 = new Book("1", "title1", Arrays.asList("Molitz", "Quentin"), 1900, "city1", "publisher1", 0);
        Book book2 = new Book("2", "title2", Arrays.asList("Emily", "Nora"), 1900, "city2", "publisher2", 0);

        BookCopy bookCopy1 = new BookCopy(1, book1);
        BookCopy bookCopy2 = new BookCopy(2, book2);
        BookCopy bookCopy3 = new BookCopy(3, book1);
        BookCopy bookCopy4 = new BookCopy(4, book1);
        BookCopy bookCopy5 = new BookCopy(5, book2);
        BookCopy bookCopy6 = new BookCopy(6, book1);
        copy1 = bookCopy1;

        Database.INSTANCE.getCustomers().addAll(Arrays.asList(customer1, customer2));
        Database.INSTANCE.getBooks().addAll(Arrays.asList(book1, book2));
        Database.INSTANCE.getBookCopies().addAll(Arrays.asList(bookCopy1, bookCopy2, bookCopy3, bookCopy4, bookCopy5, bookCopy6));

    }

    @Test
    public void searchBookCopyByTitleWasSuccessful() {
        List<BookCopy> bookCopyList = dataAccessService.searchBookCopyByTitle("title1");
        assertEquals("title1", dataAccessService.searchBookCopyByTitle("title1").get(0).getBook().getTitle());
    }

    @Test
    public void searchBookCopyByTitleWasNotSuccessful() {
        assertTrue(dataAccessService.searchBookCopyByAuthor("SOOOS").isEmpty());
    }


    @Test
    public void searchBookCopyByAuthorTest() {
        List<BookCopy> list = dataAccessService.searchBookCopyByAuthor("Quentin");

        assertEquals(1, list.get(0).getId());
        assertEquals(3, list.get(1).getId());

        assertTrue(dataAccessService.searchBookCopyByAuthor("LOL").isEmpty());
    }


    @Test
    public void lendCopySuccessful() throws BookCopyNotFoundException, InvalidStateException, CustomerNotFoundException {
        dataAccessService.lendBookCopy(1, 1);
        assertTrue(copy1.isLent());
        assertEquals(copy1, cust1.getBookCopies().get(0));
    }

    @Test
    public void lendBookCopyThrowsCustomerNotFoundException() {
        assertThrows(CustomerNotFoundException.class, () -> dataAccessService.lendBookCopy(5, 1));
    }

    @Test
    public void lendBookCopyThrowsBookCopyNotFoundException() {
        assertThrows(BookCopyNotFoundException.class, () -> dataAccessService.lendBookCopy(1, -1));
    }

    @Test
    public void lendBookCopyIsAlreadyLentThrowsInvalidStateException() throws BookCopyNotFoundException, InvalidStateException, CustomerNotFoundException {
        dataAccessService.lendBookCopy(1,1);
        assertThrows(InvalidStateException.class, () -> dataAccessService.lendBookCopy(2, 1));
    }

    @Test
    public void lendBookCopyUnpaidFeesThrowsInvalidStateException() {
        cust1.setFeesPayed(false);
        assertThrows(InvalidStateException.class, () -> dataAccessService.lendBookCopy(1, 1));
    }

    @Test
    public void lendBookCopyReachedLimitThrowsInvalidStateException() throws BookCopyNotFoundException, InvalidStateException, CustomerNotFoundException {
        dataAccessService.lendBookCopy(1, 1);
        dataAccessService.lendBookCopy(1, 2);
        dataAccessService.lendBookCopy(1, 3);
        dataAccessService.lendBookCopy(1, 4);
        dataAccessService.lendBookCopy(1, 5);
        assertThrows(InvalidStateException.class, () -> dataAccessService.lendBookCopy(1, 6));
    }

    @Test
    public void SearchBookCopyByISBNFound() {
        List<BookCopy> result = dataAccessService.searchBookCopyByISBN("1");
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(4, result.size());
    }

    @Test
    public void SearchBookCopyByISBNNotFound(){
        List<BookCopy> result = dataAccessService.searchBookCopyByISBN("1111111");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void returnBookCopyTest() throws BookCopyNotFoundException, InvalidStateException, CustomerNotFoundException {
        dataAccessService.lendBookCopy(1, 1);
        assertTrue(copy1.isLent());
        assertFalse(cust1.getBookCopies().isEmpty());

        dataAccessService.returnBookCopy(1,1);
        assertFalse(copy1.isLent());
        assertTrue(cust1.getBookCopies().isEmpty());
    }

    @Test
    public void returnBookCopyTestThrowsBookCopyNotFoundException() {
        assertThrows(BookCopyNotFoundException.class, () -> dataAccessService.returnBookCopy(1, 30));
    }

    @Test
    public void returnBookCopyTestThrowsCustomerNotFoundException(){
        assertThrows(CustomerNotFoundException.class, () -> dataAccessService.returnBookCopy(648868, 1));
    }

    @Test
    public void returnBookCopyExceededLoanPeriodLimit() throws BookCopyNotFoundException, InvalidStateException, CustomerNotFoundException {
        dataAccessService.lendBookCopy(1, 1);
        copy1.setLentDate(Date.from(Instant.now().minus(200, ChronoUnit.DAYS)));
        dataAccessService.returnBookCopy(1, 1);
        assertFalse(cust1.isFeesPayed());
        assertFalse(copy1.isLent());
        assertTrue(cust1.getBookCopies().isEmpty());
    }
}