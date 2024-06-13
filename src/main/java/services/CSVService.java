package services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import database.Database;
import entities.Book;
import entities.BookCopy;
import entities.Customer;
import exceptions.InvalidStateException;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class provides functionalities to import data from CSV files
 * into the library system. It offers methods to import book copies,
 * customers, and books themselves.
 */
public class CSVService {

    /**
     * Imports book copies from a CSV file located at the provided filePath.
     * The CSV file is expected to have ISBN in the first column.
     *
     * @param filePath The path to the CSV file containing book copy data.
     * @throws IOException           If there is an error reading the CSV file.
     * @throws CsvException          If there is an error parsing the CSV file.
     * @throws InvalidStateException If a book with the provided ISBN cannot be found in the database.
     */
    public void importBookCopiesViaCSV(String filePath) throws IOException, CsvException, InvalidStateException, ParseException {
        List<List<String>> bookCopies = readCSVFile(filePath);
        for (List<String> bookCopy : bookCopies) {
            Book book = Database.INSTANCE.getBooks().stream()
                    .filter(b -> b.getIsbn().equals(bookCopy.get(1)))
                    .findFirst()
                    .orElseThrow(() -> new InvalidStateException("No book with given ISBN found"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date importDate = format.parse(bookCopy.get(3));

            if (bookCopy.get(4).equals("yes")) {
                Date lentDate = format.parse(bookCopy.get(5));
                Database.INSTANCE.getBookCopies().add(new BookCopy(Long.parseLong(bookCopy.get(0)), book, importDate, true, lentDate));
            } else
                Database.INSTANCE.getBookCopies().add(new BookCopy(Long.parseLong(bookCopy.get(0)), book, importDate, false));

        }
        Database.INSTANCE.sortDB();
    }

    /**
     * Imports customers from a CSV file located at the provided filePath.
     * The CSV file is expected to have name, email, phone number, and address information in the following order.
     *
     * @param filePath The path to the CSV file containing customer data.
     * @throws IOException  If there is an error reading the CSV file.
     * @throws CsvException If there is an error parsing the CSV file.
     */
    public void importCustomersViaCSV(String filePath) throws IOException, CsvException {
        List<List<String>> customers = readCSVFile(filePath);
        boolean feesPayed = false;
        for (List<String> customer : customers) {
            if (customer.get(6).equals("yes"))
                feesPayed = true;
            if (customer.get(6).equals("no"))
                feesPayed = false;
            Database.INSTANCE.getCustomers().add(new Customer(Long.parseLong(customer.get(0)),
                    new ArrayList<BookCopy>(), customer.get(1),
                    customer.get(2), customer.get(3), customer.get(4), customer.get(5), feesPayed));
        }
        Database.INSTANCE.sortDB();
    }

    /**
     * Imports books from a CSV file located at the provided filePath.
     * The CSV file is expected to have ISBN, title, comma-separated author names, publication year, publisher, genre, and quantity in stock information in the following order.
     *
     * @param filePath The path to the CSV file containing book data.
     * @throws IOException  If there is an error reading the CSV file.
     * @throws CsvException If there is an error parsing the CSV file.
     */
    public void importBooksViaCSV(String filePath) throws IOException, CsvException {
        List<List<String>> books = readCSVFile(filePath);
        for (List<String> book : books) {
            Database.INSTANCE.getBooks()
                    .add(new Book(book.get(0), book.get(1), Arrays.stream(book.get(2).split("/")).toList(), //we assume Authors are split via /
                            Integer.parseInt(book.get(3)), book.get(4), book.get(5), Integer.parseInt(book.get(6))));
        }
        Database.INSTANCE.sortDB();
    }

    /**
     * Reads data from a CSV file located at the provided filePath.
     *
     * @param filePath The path to the CSV file.
     * @return A list of lists containing the parsed CSV data.
     * @throws IOException  If there is an error reading the CSV file.
     * @throws CsvException If there is an error parsing the CSV file.
     */
    private List<List<String>> readCSVFile(String filePath) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> data = reader.readAll();
        List<List<String>> processedData = new ArrayList<>();
        for (String[] line : data) {
            if(line.length == 0)
                continue;
            processedData.add(Arrays.asList(line));
        }
        reader.close();
        return processedData;
    }
}