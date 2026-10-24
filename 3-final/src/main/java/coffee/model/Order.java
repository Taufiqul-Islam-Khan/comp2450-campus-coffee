package coffee.model;

import coffee.exceptions.InvalidCupCountException;
import java.math.BigDecimal;
import java.util.Objects;

/** One order: a drink and how many cups. Always valid once it exists. */
public class Order {
    public static final int MAX_CUPS = 10;

    private final Drink drink;
    private final int cups;

    public Order(Drink drink, int cups) throws InvalidCupCountException {
        Objects.requireNonNull(drink, "drink must not be null");   // our bug if null
        if (cups < 1 || cups > MAX_CUPS) {                           // Maya can cause this
            throw new InvalidCupCountException("cups was " + cups);
        }
        this.drink = drink;
        this.cups = cups;
    }

    public Drink drink() { return drink; }
    public int cups() { return cups; }

    public BigDecimal total() {
        return drink.price().multiply(BigDecimal.valueOf(cups));
    }
}
