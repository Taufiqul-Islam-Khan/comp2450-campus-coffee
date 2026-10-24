package coffee.exceptions;

/** The customer paid less than the total. */
public class NotEnoughMoneyException extends Exception {
    public NotEnoughMoneyException() { }

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
