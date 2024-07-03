package services;

import database.Database;
import exceptions.InvalidStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class CSVServiceTest {

    CSVService csvService = new CSVService();

    /*
    Successful import of books from a CSV file.
    Attempt to import of books from a faulty CSV file and expect an ArrayIndexOutOfBoundsException.
    Successful import of book copies from a CSV file.
    Attempt to import of book copies from a CSV file without previously imported books and expect an InvalidStateException.
    Attempt to import of book copies from a faulty CSV file and expect a ParseException.
    Successful import of customers from a CSV file.
    Attempt to import of customers from a faulty CSV file and expect a NumberFormatException.
    Attempt to import customers from a non-existent CSV file (IOException).
 */
    @BeforeEach
    public void setup() {
        Database.INSTANCE.getBooks().clear();
        Database.INSTANCE.getBookCopies().clear();
        Database.INSTANCE.getCustomers().clear();
    }


    @Test
    public void ImportBooksViaCSVThrowsNoException() {
        String bookFilepath = "src/test/resources/buecher.csv";
        assertDoesNotThrow(() -> csvService.importBooksViaCSV(bookFilepath));
        assertEquals(3, Database.INSTANCE.getBooks().size());
        assertEquals("3608987010", Database.INSTANCE.getBooks().get(0).getIsbn());

    }

    @Test
    public void ImportBooksViaCSVThrowsException() {
        String bookFilepath = "src/test/resources/buecherFehlerDatei.csv";
        assertThrows(NumberFormatException.class, () -> csvService.importBooksViaCSV(bookFilepath));
        assertEquals(2, Database.INSTANCE.getBooks().size());
        assertEquals("3551551677", Database.INSTANCE.getBooks().get(0).getIsbn());

    }

    @Test
    public void ImportBookCopiesViaCSVThrowsNoException() {
        String bookFilepath = "src/test/resources/buecher.csv";
        String filePath = "src/test/resources/buchkopien.csv";
        String customerFilePath = "src/test/resources/benutzer.csv";
        assertDoesNotThrow(() -> csvService.importBooksViaCSV(bookFilepath));
        assertDoesNotThrow(() -> csvService.importCustomersViaCSV(customerFilePath));
        assertDoesNotThrow(() -> csvService.importBookCopiesViaCSV(filePath));
        assertEquals(2, Database.INSTANCE.getBookCopies().size());
        assertEquals("Title: Per Anhalter durch die Galaxis: Band 1 der fuenfbaendigen \"Intergalaktischen Trilogie\", " +
                        "Author(en): [Douglas Adams ], ISBN: 3036959548, ID: 1, Shelf location: SF42, Lent status: false, Lent Date: null | [BookCopy]\n",
                Database.INSTANCE.getBookCopies().get(0).toString());
        assertEquals(1, Database.INSTANCE.getCustomers().get(1).getBookCopies().size());
    }

    @Test
    public void ImportBookCopiesViaCSVThrowsExceptionNoBookFound() {
        String filePath = "src/test/resources/buchkopien.csv";
        assertThrows(InvalidStateException.class, () -> csvService.importBookCopiesViaCSV(filePath));
        assertEquals(0, Database.INSTANCE.getBookCopies().size());
    }

    @Test
    public void ImportBookCopiesViaCSVThrowsParseException() {
        String filePath = "src/test/resources/buchkopienFehlerDatei.csv";
        String bookFilePath = "src/test/resources/buecher.csv";
        assertDoesNotThrow(() -> csvService.importBooksViaCSV(bookFilePath));
        assertThrows(ParseException.class, () -> csvService.importBookCopiesViaCSV(filePath));
        assertEquals(1, Database.INSTANCE.getBookCopies().size());
    }

    @Test
    public void ImportBookCopiesViaCSVThrowsInvalidStateException() {
        String filePath = "src/test/resources/buchkopien.csv";
        String bookFilePath = "src/test/resources/buecher.csv";
        assertDoesNotThrow(() -> csvService.importBooksViaCSV(bookFilePath));
        assertThrows(InvalidStateException.class, () -> csvService.importBookCopiesViaCSV(filePath));
        assertEquals(1, Database.INSTANCE.getBookCopies().size());
    }

    @Test
    public void ImportCustomerViaCSVThrowsNoException() {
        String filePath = "src/test/resources/benutzer.csv";
        assertDoesNotThrow(() -> csvService.importCustomersViaCSV(filePath));
        assertEquals(2, Database.INSTANCE.getCustomers().size());
        assertEquals("ID: 2, Last name: Miller, First name: Alice, FeesPayed-Status: false, Number of books lent: 0 | [Customer]\n", Database.INSTANCE.getCustomers().get(0).toString());
    }

    @Test
    public void ImportCustomerViaCSVThrowsFormatException() {
        String filePath = "src/test/resources/benutzerFehlerDatei.csv";
        assertDoesNotThrow(() -> csvService.importCustomersViaCSV(filePath));
        assertEquals(1, Database.INSTANCE.getCustomers().size());
    }

    @Test
    public void ImportCustomerViaCSVThrowsException() {
        String filePath = "";
        assertThrows(FileNotFoundException.class, () -> csvService.importCustomersViaCSV(filePath));
    }

    @Test
    public void importCustomersViaCSVHandlesEmptyFile() {
        String filePath = "src/test/resources/empty.csv";
        assertThrows(NullPointerException.class, () -> csvService.importCustomersViaCSV(filePath));
        assertEquals(0, Database.INSTANCE.getCustomers().size());
    }
}