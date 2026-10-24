package coffee.exceptions;

/** The number of cups is outside 1 to 10. */
public class InvalidCupCountException extends Exception {
    public InvalidCupCountException() { }

    public InvalidCupCountException(String message) {
        super(message);
    }
}
