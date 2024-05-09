import database.Database;
import ui.UI;

public class Main {

    public static void main(String[] args) {
        Database.INSTANCE.init();
        UI ui = new UI();
    }
}
