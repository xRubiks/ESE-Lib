import com.opencsv.exceptions.CsvException;
import exceptions.InvalidStateException;
import services.CSVService;
import ui.UI;

import java.io.IOException;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, CsvException, InvalidStateException, ParseException {
        CSVService csvService = new CSVService();
        String bookFilepath = "src/test/resources/buecher.csv";
        String filePath = "src/test/resources/buchkopien.csv";
        String customerFilePath = "src/test/resources/benutzer.csv";
        csvService.importBooksViaCSV(bookFilepath);
        csvService.importCustomersViaCSV(customerFilePath);
        csvService.importBookCopiesViaCSV(filePath);
        UI ui = new UI();
    }
}
