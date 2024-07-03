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
        HashMap<String, List<String>> bookCopiesMap = readCsvFileViaMap(filePath);

        List<String> ids = bookCopiesMap.get("id");
        List<String> isbns = bookCopiesMap.get("bookIsbn");
        List<String> shelfLocations = bookCopiesMap.get("shelfLocation");
        List<String> addedToLibraryDates = bookCopiesMap.get("addedToLibrary");
        List<String> lentStatuses = bookCopiesMap.get("lent");
        List<String> lentDates = bookCopiesMap.get("lentDate");
        List<String> customerIds = bookCopiesMap.get("customerId");

        for (int i = 0; i < ids.size(); i++) {
            int finalI = i;
            Book book = Database.INSTANCE.getBooks().stream()
                    .filter(b -> b.getIsbn().equals(isbns.get(finalI)))
                    .findFirst()
                    .orElseThrow(() -> new InvalidStateException("No book with given ISBN found"));

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date importDate = format.parse(addedToLibraryDates.get(i));

            if (lentStatuses.get(i).equals("yes")) {
                Date lentDate = format.parse(lentDates.get(i));
                long customerId = Long.parseLong(customerIds.get(i));
                BookCopy copy = new BookCopy(Long.parseLong(ids.get(i)), book, importDate, shelfLocations.get(i), true, lentDate);
                Customer customer = Database.INSTANCE.getCustomers().stream()
                        .filter(cust -> cust.getId() == customerId)
                        .findFirst()
                        .orElseThrow(() -> new InvalidStateException(String.format("No customer with given id found: [%s] \n BookCopy: [%s] cannot be imported", customerId, copy)));
                customer.getBookCopies().add(copy);
                Database.INSTANCE.getBookCopies().add(copy);
            } else {
                Database.INSTANCE.getBookCopies().add(new BookCopy(Long.parseLong(ids.get(i)), book, importDate, shelfLocations.get(i), false));
            }
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
        HashMap<String, List<String>> customersMap = readCsvFileViaMap(filePath);

        List<String> ids = customersMap.get("id");
        List<String> names = customersMap.get("name");
        List<String> firstNames = customersMap.get("firstName");
        List<String> zipCode = customersMap.get("zipCode");
        List<String> city = customersMap.get("city");
        List<String> feesStatus = customersMap.get("feesPayed");

        for (int i = 0; i < ids.size(); i++) {
            boolean feesPayed = "yes".equalsIgnoreCase(feesStatus.get(i));
            try {
                Database.INSTANCE.getCustomers().add(new Customer(
                        Long.parseLong(ids.get(i)),
                        new ArrayList<>(),
                        names.get(i),
                        firstNames.get(i),
                        zipCode.get(i),
                        city.get(i),
                        feesStatus.get(i),
                        feesPayed
                ));
            } catch (Exception e) {
                System.err.println("Error processing customer data: " + names.get(i));
                System.err.println("Error: " + e.getMessage());
            }
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
        HashMap<String, List<String>> booksMap = readCsvFileViaMap(filePath);
        List<String> isbns = booksMap.get("isbn");
        List<String> titles = booksMap.get("title");
        List<String> authorsList = booksMap.get("authors");
        List<String> publicationYears = booksMap.get("year");
        List<String> city = booksMap.get("city");
        List<String> publisher = booksMap.get("publisher");
        List<String> edition = booksMap.get("edition");

        for (int i = 0; i < isbns.size(); i++) {
            List<String> authors = Arrays.stream(authorsList.get(i).split("/")).toList();
            Database.INSTANCE.getBooks().add(new Book(
                    isbns.get(i),
                    titles.get(i),
                    authors,
                    Integer.parseInt(publicationYears.get(i)),
                    city.get(i),
                    publisher.get(i),
                    Integer.parseInt(edition.get(i))
            ));
        }
        Database.INSTANCE.sortDB();
    }

    /**
     * Reads data from a CSV file located at the provided filePath.
     *
     * @param filePath The path to the CSV file.
     * @return A HashMap containing the parsed CSV data with headers as keys.
     * @throws IOException  If there is an error reading the CSV file.
     * @throws CsvException If there is an error parsing the CSV file.
     */
    public HashMap<String, List<String>> readCsvFileViaMap(String filePath) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(filePath));
        List<String[]> data = reader.readAll();
        HashMap<String, List<String>> hashMap = new HashMap<>();

        if (!data.isEmpty()) {
            String[] headers = data.get(0);
            for (String header : headers) {
                hashMap.put(header, new ArrayList<>());
            }
            for (int i = 1; i < data.size(); i++) {
                String[] line = data.get(i);
                for (int j = 0; j < headers.length; j++) {
                    String value = j < line.length ? line[j] : "";
                    hashMap.get(headers[j]).add(value);
                }
            }
        }
        reader.close();
        return hashMap;
    }
}
