package exceptions;

public class CustomerNotFoundException extends Exception {

    public CustomerNotFoundException(String message) {
        super(message);
        System.out.println(message);
    }
}
