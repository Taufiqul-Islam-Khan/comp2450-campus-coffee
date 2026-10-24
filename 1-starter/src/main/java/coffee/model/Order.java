package coffee.model;

import java.math.BigDecimal;

/** One order: a drink and how many cups. (Starter: no checks yet.) */
public class Order {
    private final Drink drink;
    private final int cups;

    public Order(Drink drink, int cups) {
        this.drink = drink;
        this.cups = cups;
    }

    public Drink drink() { return drink; }
    public int cups() { return cups; }

    public BigDecimal total() {
        return drink.price().multiply(BigDecimal.valueOf(cups));
    }
}
