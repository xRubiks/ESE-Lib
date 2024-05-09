import database.Database;
import ui.UI;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        Database.INSTANCE.init();
        UI ui = new UI();
    }
}
