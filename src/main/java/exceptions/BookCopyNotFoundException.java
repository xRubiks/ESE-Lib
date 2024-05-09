package exceptions;

public class BookCopyNotFoundException extends Exception {
    public BookCopyNotFoundException(String message) {
        super(message);
    }
}
