package exceptions;

public class InvalidStateException extends Exception {

    public InvalidStateException(String message) {
        super(message);
        System.out.println(message);
    }
}
