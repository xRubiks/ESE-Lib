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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReportingServiceTest {

    private final ReportingService reportingService = new ReportingService();
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;


    @BeforeEach
    public void setUp() {
        Database.INSTANCE.getCustomers().clear();
        Database.INSTANCE.getBooks().clear();
        Database.INSTANCE.getBookCopies().clear();

        Customer customer1 = new Customer(1, new ArrayList<>(), "Mairle", "Molitz", "Auf der Farm 1", "007007", "Sturgard", true);
        Customer customer2 = new Customer(2, new ArrayList<>(), "Hadar", "Quentin", "Auf der Lauer 3", "00000", "Suttgart", true);

        Book book1 = new Book("1", "title1", Arrays.asList("Molitz", "Quentin"), 1900, "city1", "publisher1", 0);
        Book book2 = new Book("2", "title2", Arrays.asList("Emily", "Nora"), 1900, "city2", "publisher2", 0);

        BookCopy bookCopy1 = new BookCopy(1, book1, new Date(), false);
        BookCopy bookCopy2 = new BookCopy(2, book2, new Date(), false);

        Database.INSTANCE.getCustomers().addAll(Arrays.asList(customer1, customer2));
        Database.INSTANCE.getBooks().addAll(Arrays.asList(book1, book2));
        Database.INSTANCE.getBookCopies().addAll(Arrays.asList(bookCopy1, bookCopy2));

        outputStreamCaptor.reset();
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void testPrintAllBooks() {
        reportingService.printAllBooks();

        String expectedOutput = null;
        if (OSCheck.isUnix())
            expectedOutput = "Title: title1, Author(en): [Molitz, Quentin], Year: 1900, ISBN: 1 | [Book]\n\nTitle: title2, Author(en): [Emily, Nora], Year: 1900, ISBN: 2 | [Book]";
        else if (OSCheck.isWindows())
            expectedOutput = "Title: title1, Author(en): [Molitz, Quentin], Year: 1900, ISBN: 1 | [Book]\n\r\nTitle: title2, Author(en): [Emily, Nora], Year: 1900, ISBN: 2 | [Book]";

        String actualOutput = outputStreamCaptor.toString().trim();
        System.setOut(originalOut);
        System.out.println("Final Output:\n" + actualOutput + "\n");

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintAllCustomers() {
        reportingService.printAllCustomers();
        String expectedOutput = null;
        if (OSCheck.isUnix())
            expectedOutput = "ID: 1, Last name: Mairle, First name: Molitz, FeesPayed-Status: true, Number of books lent: 0 | [Customer]\n\nID: 2, Last name: Hadar, First name: Quentin, FeesPayed-Status: true, Number of books lent: 0 | [Customer]";
        else if (OSCheck.isWindows())
            expectedOutput = "ID: 1, Last name: Mairle, First name: Molitz, FeesPayed-Status: true, Number of books lent: 0 | [Customer]\n\r\nID: 2, Last name: Hadar, First name: Quentin, FeesPayed-Status: true, Number of books lent: 0 | [Customer]";

        String actualOutput = outputStreamCaptor.toString().trim();
        System.setOut(originalOut);
        System.out.println("Final Output:\n" + actualOutput + "\n");

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintAllNonLentCopies() {
        reportingService.printAllNonLentCopies();
        String expectedOutput = null;
        if (OSCheck.isUnix())
            expectedOutput = "Title: title1, Author(en): [Molitz, Quentin], ISBN: 1, ID: 1, Shelf location: null, Lent status: false, Lent Date: null | [BookCopy]\n\nTitle: title2, Author(en): [Emily, Nora], ISBN: 2, ID: 2, Shelf location: null, Lent status: false, Lent Date: null | [BookCopy]";
        else if (OSCheck.isWindows())
            expectedOutput = "Title: title1, Author(en): [Molitz, Quentin], ISBN: 1, ID: 1, Shelf location: null, Lent status: false, Lent Date: null | [BookCopy]\n\r\nTitle: title2, Author(en): [Emily, Nora], ISBN: 2, ID: 2, Shelf location: null, Lent status: false, Lent Date: null | [BookCopy]";
        String actualOutput = outputStreamCaptor.toString().trim();
        System.setOut(originalOut);
        System.out.println("Final Output:\n" + actualOutput + "\n");

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintLentCopies() throws BookCopyNotFoundException, InvalidStateException, CustomerNotFoundException {
        DataAccessService dataAccessService = new DataAccessService();
        dataAccessService.lendBookCopy(1, 1);
        Date lentDate1 = new Date();
        dataAccessService.lendBookCopy(1, 2);
        Date lentDate2 = new Date();

        reportingService.printAllLentCopies();

        String expectedOutput = null;
        if (OSCheck.isWindows())
            expectedOutput = "Title: title1, Author(en): [Molitz, Quentin], ISBN: 1, ID: 1, Shelf location: null, Lent status: true, Lent Date: " + lentDate1 + " | [BookCopy]\n\r\nTitle: title2, Author(en): [Emily, Nora], ISBN: 2, ID: 2, Shelf location: null, Lent status: true, Lent Date: " + lentDate2 + " | [BookCopy]";
        else if (OSCheck.isUnix())
            expectedOutput = "Title: title1, Author(en): [Molitz, Quentin], ISBN: 1, ID: 1, Shelf location: null, Lent status: true, Lent Date: " + lentDate1 + " | [BookCopy]\n\nTitle: title2, Author(en): [Emily, Nora], ISBN: 2, ID: 2, Shelf location: null, Lent status: true, Lent Date: " + lentDate2 + " | [BookCopy]";

        String actualOutput = outputStreamCaptor.toString().trim();
        System.setOut(originalOut);
        System.out.println("Final Output:\n" + actualOutput + "\n");

        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintAllCustomersCopies() throws BookCopyNotFoundException, InvalidStateException, CustomerNotFoundException {
        DataAccessService dataAccessService = new DataAccessService();
        dataAccessService.lendBookCopy(1, 1);
        Date lentDate1 = new Date();
        dataAccessService.lendBookCopy(1, 2);
        Date lentDate2 = new Date();

        reportingService.printAllLentCopies();
        String expectedOutput = null;
        if (OSCheck.isWindows())
            expectedOutput = "Title: title1, Author(en): [Molitz, Quentin], ISBN: 1, ID: 1, Shelf location: null, Lent status: true, Lent Date: " + lentDate1 + " | [BookCopy]\n\r\nTitle: title2, Author(en): [Emily, Nora], ISBN: 2, ID: 2, Shelf location: null, Lent status: true, Lent Date: " + lentDate2 + " | [BookCopy]";
        else if (OSCheck.isUnix())
            expectedOutput = "Title: title1, Author(en): [Molitz, Quentin], ISBN: 1, ID: 1, Shelf location: null, Lent status: true, Lent Date: " + lentDate1 + " | [BookCopy]\n\nTitle: title2, Author(en): [Emily, Nora], ISBN: 2, ID: 2, Shelf location: null, Lent status: true, Lent Date: " + lentDate2 + " | [BookCopy]";
        String actualOutput = outputStreamCaptor.toString().trim();
        System.setOut(originalOut);
        System.out.println("Final Output:\n" + actualOutput + "\n");

        assertEquals(expectedOutput, actualOutput);
    }

}

class OSCheck {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.contains("win"));
    }

    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("mac"));
    }
}
