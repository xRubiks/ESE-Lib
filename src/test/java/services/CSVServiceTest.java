package services;

import database.Database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CSVServiceTest {

    CSVService csvService = new CSVService();

    /*
    Successful import of book copies from a CSV file.
    Successful import of customers from a CSV file.
    Successful import of books from a CSV file.
    Attempt to import book copies from a non-existent CSV file (IOException).
    Attempt to import customers from a non-existent CSV file (IOException).
    Attempt to import books from a non-existent CSV file (IOException).
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
    public void ImportBookCopiesViaCSVThrowsNoException() {
        String bookFilepath = "src/test/resources/buecher.csv";
        String filePath = "src/test/resources/buchkopien.csv";
        assertDoesNotThrow(() -> csvService.importBooksViaCSV(bookFilepath));
        assertDoesNotThrow(() -> csvService.importBookCopiesViaCSV(filePath));
        assertEquals(2, Database.INSTANCE.getBookCopies().size());
        assertEquals("Title: Per Anhalter durch die Galaxis: Band 1 der fünfbändigen \"Intergalaktischen Trilogie\", " +
                "Author(en): [Douglas Adams ], ISBN: 3036959548, ID: 1, Shelf location: null, Lent status: false, Lent Date: null | [BookCopy]\n",
                Database.INSTANCE.getBookCopies().get(0).toString());
    }

    @Test
    public void ImportCustomerViaCSVThrowsNoException() {
        String filePath = "src/test/resources/benutzer.csv";
        assertDoesNotThrow(() -> csvService.importCustomersViaCSV(filePath));
        assertEquals(2, Database.INSTANCE.getCustomers().size());
        assertEquals("ID: 2, Last name: Miller, First name: Alice, FeesPayed-Status: false, Number of books lent: 0 | [Customer]\n", Database.INSTANCE.getCustomers().get(0).toString());
    }
}